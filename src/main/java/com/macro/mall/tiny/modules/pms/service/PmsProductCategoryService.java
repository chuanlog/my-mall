package com.macro.mall.tiny.modules.pms.service;

import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
}
