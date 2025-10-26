package com.macro.mall.tiny.modules.oms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单商品项表
 * </p>
 *
 * @author cloog
 * @since 2025-10-26
 */
@Getter
@Setter
@TableName("oms_order_item")
@ApiModel(value = "OmsOrderItem对象", description = "订单商品项表")
public class OmsOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("所属订单ID")
    private Long orderId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品图片")
    private String productImage;

    @ApiModelProperty("商品单价")
    private BigDecimal productPrice;

    @ApiModelProperty("购买数量")
    private Integer quantity;

    @ApiModelProperty("小计金额")
    private BigDecimal totalPrice;


}
