package com.macro.mall.tiny.modules.pms.service.impl;

import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;
import com.macro.mall.tiny.modules.pms.service.PmsCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品与分类缓存管理Service实现类
 * 模仿 UmsAdminCacheServiceImpl 风格
 */
@Service
public class PmsCacheServiceImpl implements PmsCacheService {
    @Autowired
    private RedisService redisService;

    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;

    // 采用带默认值的键配置，避免必须改配置文件
    @Value("${redis.key.pms.product:pms:product}")
    private String REDIS_KEY_PRODUCT;
    @Value("${redis.key.pms.category:pms:category}")
    private String REDIS_KEY_CATEGORY;
    @Value("${redis.key.pms.categoryListAll:pms:categoryListAll}")
    private String REDIS_KEY_CATEGORY_LIST_ALL;

    @Override
    public void delProduct(Long productId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PRODUCT + ":" + productId;
        redisService.del(key);
    }

    @Override
    public PmsProduct getProduct(Long productId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PRODUCT + ":" + productId;
        return (PmsProduct) redisService.get(key);
    }

    @Override
    public void setProduct(PmsProduct product) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PRODUCT + ":" + product.getId();
        redisService.set(key, product, REDIS_EXPIRE);
    }

    @Override
    public void delCategory(Long categoryId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_CATEGORY + ":" + categoryId;
        redisService.del(key);
    }

    @Override
    public PmsProductCategory getCategory(Long categoryId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_CATEGORY + ":" + categoryId;
        return (PmsProductCategory) redisService.get(key);
    }

    @Override
    public void setCategory(PmsProductCategory category) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_CATEGORY + ":" + category.getId();
        redisService.set(key, category, REDIS_EXPIRE);
    }

    @Override
    public void delCategoryListAll() {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_CATEGORY_LIST_ALL;
        redisService.del(key);
    }

    @Override
    public List<PmsProductCategory> getCategoryListAll() {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_CATEGORY_LIST_ALL;
        return (List<PmsProductCategory>) redisService.get(key);
    }

    @Override
    public void setCategoryListAll(List<PmsProductCategory> list) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_CATEGORY_LIST_ALL;
        redisService.set(key, list, REDIS_EXPIRE);
    }
}