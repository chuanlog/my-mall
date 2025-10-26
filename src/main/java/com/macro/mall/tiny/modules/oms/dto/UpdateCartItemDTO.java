package com.macro.mall.tiny.modules.oms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateCartItemDTO {
    @NotNull
    @ApiModelProperty(value = "商品id", required = true)
    private Long productId;

    @NotNull
    @ApiModelProperty(value = "设定的商品数量", required = true)
    private Integer quantity;
}