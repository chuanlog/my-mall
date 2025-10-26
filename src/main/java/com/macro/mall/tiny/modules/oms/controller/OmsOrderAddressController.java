package com.macro.mall.tiny.modules.oms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.macro.mall.tiny.modules.oms.service.OmsOrderAddressService;

/**
 * 订单收货地址表 控制器
 * Created by  cloog on 2025-10-26
 */
@Api(tags = "OmsOrderAddressController")
@Tag(name = "OmsOrderAddressController", description = "")
@Controller
@RequestMapping("/oms/omsOrderAddress")
public class OmsOrderAddressController {

    @Autowired
    private OmsOrderAddressService omsOrderAddressService;

}