package com.macro.mall.tiny.modules.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.oms.dto.AddShoppingCartDTO;
import com.macro.mall.tiny.modules.oms.model.OmsShoppingCart;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author cloog
 * @since 2025-10-26
 */
public interface OmsShoppingCartService extends IService<OmsShoppingCart> {

    boolean add(AddShoppingCartDTO addShoppingCartDTO);
}
