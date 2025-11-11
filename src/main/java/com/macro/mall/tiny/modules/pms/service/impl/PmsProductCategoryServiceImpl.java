package com.macro.mall.tiny.modules.pms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductCategoryMapper;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;
import com.macro.mall.tiny.modules.pms.service.PmsProductCategoryService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品种类表 服务实现类
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
@Service
public class PmsProductCategoryServiceImpl extends ServiceImpl<PmsProductCategoryMapper, PmsProductCategory> implements PmsProductCategoryService {

    // 引入缓存服务获取
    private com.macro.mall.tiny.modules.pms.service.PmsCacheService getCacheService() {
        return com.macro.mall.tiny.security.util.SpringUtil.getBean(com.macro.mall.tiny.modules.pms.service.PmsCacheService.class);
    }

    @Override
    public boolean create(PmsProductCategory category) {
        Date now = new Date();
        category.setCreateTime(now);
        category.setUpdateTime(now);
        if (category.getSort() == null) {
            category.setSort(0);
        }
        boolean success = save(category);
        if (success) {
            // 新增后清理全量列表缓存
            getCacheService().delCategoryListAll();
        }
        return success;
    }

    @Override
    public boolean delete(List<Long> ids) {
        boolean success = removeByIds(ids);
        if (success && ids != null) {
            for (Long id : ids) {
                getCacheService().delCategory(id);
            }
            getCacheService().delCategoryListAll();
        }
        return success;
    }

    @Override
    public Page<PmsProductCategory> list(String keyword, Integer pageSize, Integer pageNum) {
        // 保持原分页查询逻辑（不做复杂条件缓存）
        Page<PmsProductCategory> page = new Page<>(pageNum, pageSize);
        QueryWrapper<PmsProductCategory> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmsProductCategory> lambda = wrapper.lambda();
        if (cn.hutool.core.util.StrUtil.isNotEmpty(keyword)) {
            lambda.like(PmsProductCategory::getName, keyword);
        }
        lambda.orderByAsc(PmsProductCategory::getSort).orderByDesc(PmsProductCategory::getUpdateTime);
        return page(page, wrapper);
    }

    @Override
    public PmsProductCategory getCategoryById(Long id) {
        PmsProductCategory cached = getCacheService().getCategory(id);
        if (cached != null) {
            return cached;
        }
        PmsProductCategory db = getById(id);
        if (db != null) {
            getCacheService().setCategory(db);
        }
        return db;
    }

    @Override
    public List<PmsProductCategory> listAllCached() {
        List<PmsProductCategory> cached = getCacheService().getCategoryListAll();
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }
        List<PmsProductCategory> all = list();
        if (all != null && !all.isEmpty()) {
            getCacheService().setCategoryListAll(all);
        }
        return all;
    }

    @Override
    public boolean updateCategory(Long id, PmsProductCategory category) {
        category.setId(id);
        boolean success = updateById(category);
        if (success) {
            // 更新后清理对应分类缓存以及全量列表缓存
            getCacheService().delCategory(id);
            getCacheService().delCategoryListAll();
        }
        return success;
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.macro.mall.tiny.common.utils.MinioUtil minioUtil;

    @Override
    public String uploadImage(Long categoryId, org.springframework.web.multipart.MultipartFile file) {
        try {
            PmsProductCategory category = getById(categoryId);
            if (category == null) {
                throw new RuntimeException("商品分类不存在");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("只能上传图片文件");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String objectName = "images/category/" + categoryId + "_" + System.currentTimeMillis() + extension;

            String url = minioUtil.uploadFile(file, objectName);

            // 删除旧图片（如果存在且属于本模块路径）
            if (cn.hutool.core.util.StrUtil.isNotBlank(category.getImage())) {
                String oldObjectName = extractObjectNameFromUrl(category.getImage(), "images/category/");
                if (cn.hutool.core.util.StrUtil.isNotBlank(oldObjectName)) {
                    minioUtil.deleteFile(oldObjectName);
                }
            }

            category.setImage(url);
            updateById(category);
            return url;
        } catch (Exception e) {
            throw new RuntimeException("上传分类图片失败: " + e.getMessage());
        }
    }


    private String extractObjectNameFromUrl(String url, String prefix) {
        if (cn.hutool.core.util.StrUtil.isBlank(url)) {
            return null;
        }
        int index = url.indexOf(prefix);
        if (index != -1) {
            String objectName = url.substring(index);
            int queryIndex = objectName.indexOf("?");
            if (queryIndex != -1) {
                objectName = objectName.substring(0, queryIndex);
            }
            return objectName;
        }
        return null;
    }
}
