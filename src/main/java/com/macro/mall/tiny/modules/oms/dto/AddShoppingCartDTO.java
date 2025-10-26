package com.macro.mall.tiny.modules.oms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = false)
public class AddShoppingCartDTO {
    @NotEmpty
    @ApiModelProperty(value = "商品id", required = true)
    Long productId;

    @NotEmpty
    @ApiModelProperty(value = "商品数量", required = true)
    Integer quantity;
}
