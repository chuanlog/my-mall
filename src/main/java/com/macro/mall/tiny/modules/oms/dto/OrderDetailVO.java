package com.macro.mall.tiny.modules.oms.dto;

import com.macro.mall.tiny.modules.oms.model.OmsOrder;
import com.macro.mall.tiny.modules.oms.model.OmsOrderAddress;
import com.macro.mall.tiny.modules.oms.model.OmsOrderItem;
import com.macro.mall.tiny.modules.oms.model.OmsOrderPayment;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailVO {
    private OmsOrder order;
    private List<OmsOrderItem> items;
    private OmsOrderAddress address;
    private List<OmsOrderPayment> payments;
}