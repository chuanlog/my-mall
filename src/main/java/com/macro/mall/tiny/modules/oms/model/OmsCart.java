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
 * 购物车表
 * </p>
 *
 * @author cloog
 * @since 2025-10-26
 */
@Getter
@Setter
@TableName("oms_cart")
@ApiModel(value = "OmsCart对象", description = "购物车表")
public class OmsCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    private Long memberId;

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

    @ApiModelProperty("加入购物车时间")
    private Date createTime;


}
