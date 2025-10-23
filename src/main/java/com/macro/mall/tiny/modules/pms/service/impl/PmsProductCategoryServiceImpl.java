package com.macro.mall.tiny.modules.pms.service.impl;

import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductCategoryMapper;
import com.macro.mall.tiny.modules.pms.service.PmsProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public boolean create(PmsProductCategory category) {
        Date now = new Date();
        category.setCreateTime(now);
        category.setUpdateTime(now);
        if (category.getSort() == null) {
            category.setSort(0);
        }
        return save(category);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Page<PmsProductCategory> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<PmsProductCategory> page = new Page<>(pageNum, pageSize);
        QueryWrapper<PmsProductCategory> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmsProductCategory> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(PmsProductCategory::getName, keyword);
        }
        lambda.orderByAsc(PmsProductCategory::getSort).orderByDesc(PmsProductCategory::getUpdateTime);
        return page(page, wrapper);
    }
}
