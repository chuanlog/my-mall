package com.macro.mall.tiny.modules.oms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CancelOrderDTO {
    @ApiModelProperty("订单ID")
    private Long orderId;
}