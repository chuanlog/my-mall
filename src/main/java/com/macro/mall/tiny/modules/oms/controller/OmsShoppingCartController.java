package com.macro.mall.tiny.modules.oms.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.oms.dto.AddShoppingCartDTO;
import com.macro.mall.tiny.modules.oms.service.OmsShoppingCartService;
import com.macro.mall.tiny.modules.pms.service.PmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 购物车表 控制器
 * Created by  cloog on 2025-10-26
 */
@Api(tags = "OmsShoppingCartController")
@Tag(name = "OmsShoppingCartController", description = "")
@Controller
@RequestMapping("/oms/omsShoppingCart")
public class OmsShoppingCartController {

    @Autowired
    private OmsShoppingCartService omsShoppingCartService;

    @Autowired
    private PmsProductService pmsProductService;


    @ApiOperation("添加购物车")
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult add(@RequestBody @Validated AddShoppingCartDTO addShoppingCartDTO) {
        //参数校验
        if(addShoppingCartDTO.getProductId() == null|| addShoppingCartDTO.getQuantity() == null){
            return CommonResult.failed("为输入商品id商品数量");
        }

        if(addShoppingCartDTO.getQuantity() <= 0){
            return CommonResult.failed("商品数量不能小于0");
        }

        //非空校验
        if(pmsProductService.getById(addShoppingCartDTO.getProductId()) == null){
            return CommonResult.failed("商品不存在");
        }

        // 添加
        boolean success = omsShoppingCartService.add(addShoppingCartDTO);

        return success ? CommonResult.success(null) : CommonResult.failed();

    }

}