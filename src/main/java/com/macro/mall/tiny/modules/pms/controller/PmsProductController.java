package com.macro.mall.tiny.modules.pms.controller;


import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author macro
 * @since 2025-10-23
 */
@Controller
@Api(tags = "PmsProductController")
@Tag(name = "PmsProductController", description = "商品表管理")
@RequestMapping("/pms/product")
public class PmsProductController {

}

