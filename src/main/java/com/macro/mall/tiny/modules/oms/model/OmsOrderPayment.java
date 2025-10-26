package com.macro.mall.tiny.modules.oms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单支付记录表
 * </p>
 *
 * @author cloog
 * @since 2025-10-26
 */
@Getter
@Setter
@TableName("oms_order_payment")
@ApiModel(value = "OmsOrderPayment对象", description = "订单支付记录表")
public class OmsOrderPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("所属订单ID")
    private Long orderId;

    @ApiModelProperty("支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("支付方式：0->未支付；1->支付宝；2->微信；3->银行卡")
    private Boolean payType;

    @ApiModelProperty("支付时间")
    private Date payTime;


}
