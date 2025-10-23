package com.macro.mall.tiny.modules.pms.service.impl;

import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductMapper;
import com.macro.mall.tiny.modules.pms.service.PmsProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {

    @org.springframework.beans.factory.annotation.Autowired
    private com.macro.mall.tiny.common.utils.MinioUtil minioUtil;

    @Override
    public boolean create(PmsProduct product) {
        Date now = new Date();
        product.setCreateTime(now);
        product.setUpdateTime(now);
        if (product.getSort() == null) {
            product.setSort(0);
        }
        return save(product);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Page<PmsProduct> list(String keyword, Long categoryId, Boolean status, BigDecimal minPrice, BigDecimal maxPrice, Integer pageSize, Integer pageNum) {
        Page<PmsProduct> page = new Page<>(pageNum, pageSize);
        QueryWrapper<PmsProduct> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmsProduct> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(PmsProduct::getName, keyword);
        }
        if (categoryId != null) {
            lambda.eq(PmsProduct::getCategoryId, categoryId);
        }
        if (status != null) {
            lambda.eq(PmsProduct::getStatus, status);
        }
        if (minPrice != null) {
            lambda.ge(PmsProduct::getPrice, minPrice);
        }
        if (maxPrice != null) {
            lambda.le(PmsProduct::getPrice, maxPrice);
        }
        lambda.orderByAsc(PmsProduct::getSort).orderByDesc(PmsProduct::getUpdateTime);
        return page(page, wrapper);
    }

    @Override
    public String uploadImage(Long productId, org.springframework.web.multipart.MultipartFile file) {
        try {
            PmsProduct product = getById(productId);
            if (product == null) {
                throw new RuntimeException("商品不存在");
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
            String objectName = "images/product/" + productId + "_" + System.currentTimeMillis() + extension;

            String url = minioUtil.uploadFile(file, objectName);

            // 删除旧图片（如果存在且属于本模块路径）
            if (cn.hutool.core.util.StrUtil.isNotBlank(product.getImage())) {
                String oldObjectName = extractObjectNameFromUrl(product.getImage(), "images/product/");
                if (cn.hutool.core.util.StrUtil.isNotBlank(oldObjectName)) {
                    minioUtil.deleteFile(oldObjectName);
                }
            }

            product.setImage(url);
            updateById(product);
            return url;
        } catch (Exception e) {
            throw new RuntimeException("上传商品图片失败: " + e.getMessage());
        }
    }

    @Override
    public boolean updateImage(Long productId, String imageUrl) {
        try {
            PmsProduct product = getById(productId);
            if (product == null) {
                return false;
            }
            // 删除旧图片（如果存在且属于本模块路径）
            if (cn.hutool.core.util.StrUtil.isNotBlank(product.getImage())) {
                String oldObjectName = extractObjectNameFromUrl(product.getImage(), "images/product/");
                if (cn.hutool.core.util.StrUtil.isNotBlank(oldObjectName)) {
                    minioUtil.deleteFile(oldObjectName);
                }
            }
            product.setImage(imageUrl);
            return updateById(product);
        } catch (Exception e) {
            return false;
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
