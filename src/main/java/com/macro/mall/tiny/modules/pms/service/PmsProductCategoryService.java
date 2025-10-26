package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;

import java.util.List;

/**
 * <p>
 * 商品种类表 服务类
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
public interface PmsProductCategoryService extends IService<PmsProductCategory> {
    boolean create(PmsProductCategory category);

    boolean delete(List<Long> ids);

    Page<PmsProductCategory> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 上传商品分类图片
     * @param categoryId 分类ID
     * @param file 图片文件
     * @return 图片URL
     */
    String uploadImage(Long categoryId, org.springframework.web.multipart.MultipartFile file);

    /**
     * 更新商品分类图片
     * @param categoryId 分类ID
     * @param imageUrl 图片URL
     * @return 是否成功
     */
    boolean updateImage(Long categoryId, String imageUrl);

    // 新增：带缓存的获取与更新，及列表缓存
    PmsProductCategory getCategoryById(Long id);
    List<PmsProductCategory> listAllCached();
    boolean updateCategory(Long id, PmsProductCategory category);
}
