package com.macro.mall.tiny.modules.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.oms.dto.AddShoppingCartDTO;
import com.macro.mall.tiny.modules.oms.dto.CartSummaryVO;
import com.macro.mall.tiny.modules.oms.model.OmsShoppingCart;

import java.util.List;

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

    /**
     * 查询当前用户的购物车列表
     */
    List<OmsShoppingCart> listCurrent();

    /**
     * 更新某商品的购买数量（设定值）
     */
    boolean updateQuantity(Long productId, Integer quantity);

    /**
     * 删除购物车中某商品
     */
    boolean removeByProductId(Long productId);

    /**
     * 清空当前用户购物车
     */
    boolean clearCurrent();

    /**
     * 获取购物车汇总（总数量/总金额）
     */
    CartSummaryVO getSummary();
}
