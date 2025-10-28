package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户收货地址表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_member_receive_address")
@ApiModel(value = "UmsMemberReceiveAddress对象", description = "用户收货地址表")
public class UmsMemberReceiveAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("所属用户ID")
    private Long memberId;

    @ApiModelProperty("收货人名称")
    private String name;

    @ApiModelProperty("收货人电话")
    private String phone;

    @ApiModelProperty("邮政编码")
    private String postCode;

    @ApiModelProperty("省份/直辖市")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区/县")
    private String region;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelProperty("是否默认：0->否；1->是")
    private Integer defaultStatus;

    @ApiModelProperty("创建时间")
    private Date createTime;
}