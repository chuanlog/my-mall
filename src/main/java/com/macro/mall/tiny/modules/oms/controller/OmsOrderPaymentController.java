package com.macro.mall.tiny.modules.oms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.macro.mall.tiny.modules.oms.service.OmsOrderPaymentService;

/**
 * 订单支付记录表 控制器
 * Created by  cloog on 2025-10-26
 */
@Api(tags = "OmsOrderPaymentController")
@Tag(name = "OmsOrderPaymentController", description = "")
@Controller
@RequestMapping("/oms/omsOrderPayment")
public class OmsOrderPaymentController {

    @Autowired
    private OmsOrderPaymentService omsOrderPaymentService;

}