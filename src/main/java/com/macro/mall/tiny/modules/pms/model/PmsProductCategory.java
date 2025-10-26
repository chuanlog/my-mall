package com.macro.mall.tiny.modules.pms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商品种类表
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
@Getter
@Setter
@TableName("pms_product_category")
@ApiModel(value = "PmsProductCategory对象", description = "商品种类表")
public class PmsProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品种类名称")
    private String name;

    @ApiModelProperty("商品种类图片")
    private String image;

    @ApiModelProperty("商品种类描述")
    private String description;

    @ApiModelProperty("商品种类状态：0->下架；1->上架")
    private Boolean status;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;


}
