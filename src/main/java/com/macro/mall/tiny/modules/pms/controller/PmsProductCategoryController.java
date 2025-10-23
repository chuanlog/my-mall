package com.macro.mall.tiny.modules.pms.controller;


import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品种类表 前端控制器
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
@Controller
@Api(tags = "PmsProductCategoryController")
@Tag(name = "PmsProductCategoryController",description = "商品分类管理")
@RequestMapping("/pms/productCategory")

public class PmsProductCategoryController {

}

