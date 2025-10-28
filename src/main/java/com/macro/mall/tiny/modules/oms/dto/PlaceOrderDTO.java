package com.macro.mall.tiny.modules.oms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlaceOrderDTO {
    @ApiModelProperty("收货地址ID，不传则使用默认地址")
    private Long addressId;

    @ApiModelProperty("订单备注")
    private String note;
}