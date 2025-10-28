package com.macro.mall.tiny.modules.oms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayOrderDTO {
    @ApiModelProperty("订单ID")
    private Long orderId;
}