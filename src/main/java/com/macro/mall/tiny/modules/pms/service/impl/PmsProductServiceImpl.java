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
}
