package com.macro.mall.tiny.modules.pms.service;

import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;

import java.util.List;

/**
 * 商品与分类缓存管理Service
 * 模仿 UmsAdminCacheService 的风格
 */
public interface PmsCacheService {
    // 商品缓存
    void delProduct(Long productId);
    PmsProduct getProduct(Long productId);
    void setProduct(PmsProduct product);

    // 分类缓存
    void delCategory(Long categoryId);
    PmsProductCategory getCategory(Long categoryId);
    void setCategory(PmsProductCategory category);

    // 分类全量列表缓存
    void delCategoryListAll();
    List<PmsProductCategory> getCategoryListAll();
    void setCategoryListAll(List<PmsProductCategory> list);
}