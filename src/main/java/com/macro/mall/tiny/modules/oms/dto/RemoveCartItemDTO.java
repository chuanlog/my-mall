package com.macro.mall.tiny.modules.oms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RemoveCartItemDTO {
    @NotNull
    @ApiModelProperty(value = "商品id", required = true)
    private Long productId;
}