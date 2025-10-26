package com.macro.mall.tiny.modules.oms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartSummaryVO {
    @ApiModelProperty("总商品数量")
    private Integer totalQuantity;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;
}