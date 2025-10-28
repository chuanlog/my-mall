package com.macro.mall.tiny.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.utils.UserUtil;
import com.macro.mall.tiny.modules.oms.dto.OrderDetailVO;
import com.macro.mall.tiny.modules.oms.mapper.OmsOrderMapper;
import com.macro.mall.tiny.modules.oms.model.OmsOrder;
import com.macro.mall.tiny.modules.oms.model.OmsOrderAddress;
import com.macro.mall.tiny.modules.oms.model.OmsOrderItem;
import com.macro.mall.tiny.modules.oms.model.OmsOrderPayment;
import com.macro.mall.tiny.modules.oms.model.OmsShoppingCart;
import com.macro.mall.tiny.modules.oms.service.OmsOrderAddressService;
import com.macro.mall.tiny.modules.oms.service.OmsOrderItemService;
import com.macro.mall.tiny.modules.oms.service.OmsOrderPaymentService;
import com.macro.mall.tiny.modules.oms.service.OmsOrderService;
import com.macro.mall.tiny.modules.oms.service.OmsShoppingCartService;
import com.macro.mall.tiny.modules.ums.model.UmsMemberReceiveAddress;
import com.macro.mall.tiny.modules.ums.service.UmsMemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author cloog
 * @since 2025-10-26
 */
@Service
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderService {

    @Autowired
    private OmsShoppingCartService omsShoppingCartService;
    @Autowired
    private OmsOrderItemService omsOrderItemService;
    @Autowired
    private OmsOrderAddressService omsOrderAddressService;
    @Autowired
    private UmsMemberReceiveAddressService umsMemberReceiveAddressService;
    @Autowired
    private UserUtil userUtil;

    @Autowired
    private OmsOrderPaymentService omsOrderPaymentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OmsOrder placeOrder(Long addressId, String note) {
        Long memberId = userUtil.getCurrentUserId();

        // 1. 获取购物车
        List<OmsShoppingCart> carts = omsShoppingCartService.listCurrent();
        if (carts == null || carts.isEmpty()) {
            throw new IllegalStateException("购物车为空，无法下单");
        }

        // 2. 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OmsShoppingCart c : carts) {
            BigDecimal price = c.getProductPrice() == null ? BigDecimal.ZERO : c.getProductPrice();
            int qty = c.getQuantity() == null ? 0 : c.getQuantity();
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(qty)));
        }

        // 3. 确定收货地址（优先使用入参，否则取默认地址列表中第一条）
        UmsMemberReceiveAddress address = null;
        if (addressId != null) {
            address = umsMemberReceiveAddressService.getById(addressId);
            if (address == null || !memberId.equals(address.getMemberId())) {
                throw new IllegalArgumentException("收货地址无效或不属于当前用户");
            }
        } else {
            List<UmsMemberReceiveAddress> all = umsMemberReceiveAddressService.listByMemberId(memberId);
            if (all == null || all.isEmpty()) {
                throw new IllegalStateException("当前用户没有收货地址，无法下单");
            }
            address = all.get(0);
        }

        // 4. 创建订单
        Date now = new Date();
        OmsOrder order = new OmsOrder();
        order.setMemberId(memberId);
        order.setOrderSn(generateOrderSn(memberId, now));
        // Boolean 类型字段兼容：false 表示未支付/待支付
        order.setStatus(false);
        order.setPayType(false);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        order.setNote(note);
        save(order);

        // 5. 生成订单商品项
        List<OmsOrderItem> items = carts.stream().map(c -> {
            OmsOrderItem item = new OmsOrderItem();
            item.setOrderId(order.getId());
            item.setProductId(c.getProductId());
            item.setProductName(c.getProductName());
            item.setProductImage(c.getProductImage());
            item.setProductPrice(c.getProductPrice());
            item.setQuantity(c.getQuantity());
            BigDecimal price = c.getProductPrice() == null ? BigDecimal.ZERO : c.getProductPrice();
            int qty = c.getQuantity() == null ? 0 : c.getQuantity();
            item.setTotalPrice(price.multiply(BigDecimal.valueOf(qty)));
            return item;
        }).collect(Collectors.toList());
        omsOrderItemService.saveBatch(items);

        // 6. 复制收货地址到订单地址
        OmsOrderAddress orderAddress = new OmsOrderAddress();
        orderAddress.setOrderId(order.getId());
        orderAddress.setReceiverName(address.getName());
        orderAddress.setReceiverPhone(address.getPhone());
        orderAddress.setProvince(address.getProvince());
        orderAddress.setCity(address.getCity());
        orderAddress.setDistrict(address.getRegion());
        orderAddress.setDetailAddress(address.getDetailAddress());
        omsOrderAddressService.save(orderAddress);

        // 7. 清空购物车
        omsShoppingCartService.clearCurrent();

        return order;
    }

    private String generateOrderSn(Long memberId, Date time) {
        // 格式：yyyyMMddHHmmss + 用户ID后4位 + 随机2位
        String ts = new SimpleDateFormat("yyyyMMddHHmmss").format(time);
        String uid = memberId == null ? "0000" : String.format("%04d", Math.abs(memberId % 10000));
        int rand = (int) (Math.random() * 100);
        String r2 = String.format("%02d", rand);
        return ts + uid + r2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OmsOrder simulatePay(Long orderId) {
        Long memberId = userUtil.getCurrentUserId();

        OmsOrder order = getById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        if (!memberId.equals(order.getMemberId())) {
            throw new IllegalStateException("无权支付该订单");
        }
        if (Boolean.TRUE.equals(order.getStatus())) {
            throw new IllegalStateException("订单已支付，无需重复支付");
        }

        Date now = new Date();

        // 1) 更新订单为已支付（Boolean 字段：true 表示已支付）
        order.setStatus(true);
        order.setPayType(true); // 仅占位，Boolean 字段不区分具体支付方式
        order.setUpdateTime(now);
        updateById(order);

        // 2) 写入支付记录
        OmsOrderPayment payment = new OmsOrderPayment();
        payment.setOrderId(order.getId());
        payment.setPayAmount(order.getPayAmount() == null ? BigDecimal.ZERO : order.getPayAmount());
        payment.setPayType(true);
        payment.setPayTime(now);
        omsOrderPaymentService.save(payment);

        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Long orderId) {
        Long memberId = userUtil.getCurrentUserId();
        OmsOrder order = getById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        if (!memberId.equals(order.getMemberId())) {
            throw new IllegalStateException("无权取消该订单");
        }
        if (Boolean.TRUE.equals(order.getStatus())) {
            throw new IllegalStateException("订单已支付，无法取消");
        }

        // 删除子记录：项、地址、支付记录（保障幂等）
        LambdaQueryWrapper<OmsOrderItem> itemW = new LambdaQueryWrapper<OmsOrderItem>()
                .eq(OmsOrderItem::getOrderId, orderId);
        omsOrderItemService.remove(itemW);

        LambdaQueryWrapper<OmsOrderAddress> addrW = new LambdaQueryWrapper<OmsOrderAddress>()
                .eq(OmsOrderAddress::getOrderId, orderId);
        omsOrderAddressService.remove(addrW);

        LambdaQueryWrapper<OmsOrderPayment> payW = new LambdaQueryWrapper<OmsOrderPayment>()
                .eq(OmsOrderPayment::getOrderId, orderId);
        omsOrderPaymentService.remove(payW);

        // 删除订单本身
        return removeById(orderId);
    }

    @Override
    public OrderDetailVO getDetail(Long orderId) {
        Long memberId = userUtil.getCurrentUserId();
        OmsOrder order = getById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        if (!memberId.equals(order.getMemberId())) {
            throw new IllegalStateException("无权查看该订单");
        }

        List<OmsOrderItem> items = omsOrderItemService.list(
                new LambdaQueryWrapper<OmsOrderItem>().eq(OmsOrderItem::getOrderId, orderId));

        OmsOrderAddress address = omsOrderAddressService.getOne(
                new LambdaQueryWrapper<OmsOrderAddress>().eq(OmsOrderAddress::getOrderId, orderId));

        List<OmsOrderPayment> payments = omsOrderPaymentService.list(
                new LambdaQueryWrapper<OmsOrderPayment>().eq(OmsOrderPayment::getOrderId, orderId));

        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrder(order);
        vo.setItems(items);
        vo.setAddress(address);
        vo.setPayments(payments);
        return vo;
    }

    @Override
    public Page<OmsOrder> listMy(Boolean status, Integer pageSize, Integer pageNum) {
        Long memberId = userUtil.getCurrentUserId();
        Page<OmsOrder> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 5 : pageSize);
        QueryWrapper<OmsOrder> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(OmsOrder::getMemberId, memberId)
                .eq(status != null, OmsOrder::getStatus, status)
                .orderByDesc(OmsOrder::getCreateTime);
        return page(page, wrapper);
    }
}
