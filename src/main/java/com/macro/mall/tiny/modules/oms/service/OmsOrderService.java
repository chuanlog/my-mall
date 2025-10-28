package com.macro.mall.tiny.modules.oms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.oms.model.OmsOrder;
import com.macro.mall.tiny.modules.oms.dto.OrderDetailVO;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author cloog
 * @since 2025-10-26
 */
public interface OmsOrderService extends IService<OmsOrder> {
    /**
     * 下单：从当前用户购物车生成订单、订单项与订单地址
     */
    OmsOrder placeOrder(Long addressId, String note);

    /**
     * 模拟支付：直接将订单置为已支付并记录支付流水
     */
    OmsOrder simulatePay(Long orderId);

    /**
     * 取消订单（仅未支付），级联删除子记录
     */
    boolean cancel(Long orderId);

    /**
     * 查看订单详情（订单+项+地址+支付记录）
     */
    OrderDetailVO getDetail(Long orderId);

    /**
     * 分页查询我的订单，可按支付状态筛选
     */
    Page<OmsOrder> listMy(Boolean status, Integer pageSize, Integer pageNum);
}
