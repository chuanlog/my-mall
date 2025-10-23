package com.macro.mall.tiny.modules.pms.service;

import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.math.BigDecimal;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
public interface PmsProductService extends IService<PmsProduct> {
    boolean create(PmsProduct product);

    boolean delete(List<Long> ids);

    Page<PmsProduct> list(String keyword, Long categoryId, Boolean status, BigDecimal minPrice, BigDecimal maxPrice, Integer pageSize, Integer pageNum);
}
