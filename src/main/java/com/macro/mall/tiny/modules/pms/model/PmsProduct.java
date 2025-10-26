package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
@Getter
@Setter
@TableName("pms_product")
@ApiModel(value = "PmsProduct对象", description = "商品表")
public class PmsProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品种类ID")
    private Long categoryId;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品图片")
    private String image;

    @ApiModelProperty("商品标签,json格式,例如[“新品”,“精选”]")
    private String tags;

    @ApiModelProperty("商品价格")
    private BigDecimal price;

    @ApiModelProperty("商品描述")
    private String description;

    @ApiModelProperty("商品状态：0->下架；1->上架")
    private Boolean status;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;


}
