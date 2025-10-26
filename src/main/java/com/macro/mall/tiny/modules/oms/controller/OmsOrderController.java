package com.macro.mall.tiny.modules.oms.controller;

import com.macro.mall.tiny.modules.oms.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 订单表 控制器
 * Created by  cloog on 2025-10-26
 */
@Api(tags = "OmsOrderController")
@Tag(name = "OmsOrderController", description = "")
@Controller
@RequestMapping("/oms/omsOrder")
public class OmsOrderController {

    @Autowired
    private OmsOrderService omsOrderService;

}