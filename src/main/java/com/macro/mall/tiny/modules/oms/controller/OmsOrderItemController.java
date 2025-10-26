package com.macro.mall.tiny.modules.oms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.macro.mall.tiny.modules.oms.service.OmsOrderItemService;

/**
 * 订单商品项表 控制器
 * Created by  cloog on 2025-10-26
 */
@Api(tags = "OmsOrderItemController")
@Tag(name = "OmsOrderItemController", description = "")
@Controller
@RequestMapping("/oms/omsOrderItem")
public class OmsOrderItemController {

    @Autowired
    private OmsOrderItemService omsOrderItemService;

}