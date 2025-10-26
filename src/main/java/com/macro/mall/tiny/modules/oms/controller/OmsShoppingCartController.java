package com.macro.mall.tiny.modules.oms.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.oms.dto.AddShoppingCartDTO;
import com.macro.mall.tiny.modules.oms.dto.CartSummaryVO;
import com.macro.mall.tiny.modules.oms.dto.RemoveCartItemDTO;
import com.macro.mall.tiny.modules.oms.dto.UpdateCartItemDTO;
import com.macro.mall.tiny.modules.oms.model.OmsShoppingCart;
import com.macro.mall.tiny.modules.oms.service.OmsShoppingCartService;
import com.macro.mall.tiny.modules.pms.service.PmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return CommonResult.failed("未输入商品id或商品数量");
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

    @ApiOperation("当前用户购物车列表")
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<OmsShoppingCart>> list() {
        List<OmsShoppingCart> carts = omsShoppingCartService.listCurrent();
        return CommonResult.success(carts);
    }

    @ApiOperation("更新购物车某商品数量（设定值）")
    @ResponseBody
    @RequestMapping(value = "/updateQuantity", method = RequestMethod.POST)
    public CommonResult updateQuantity(@RequestBody @Validated UpdateCartItemDTO dto) {
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            return CommonResult.failed("商品数量必须为正整数");
        }
        if (pmsProductService.getById(dto.getProductId()) == null) {
            return CommonResult.failed("商品不存在");
        }
        boolean success = omsShoppingCartService.updateQuantity(dto.getProductId(), dto.getQuantity());
        return success ? CommonResult.success(null) : CommonResult.failed("购物车中不存在该商品");
    }

    @ApiOperation("删除购物车某商品")
    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonResult remove(@RequestBody @Validated RemoveCartItemDTO dto) {
        boolean success = omsShoppingCartService.removeByProductId(dto.getProductId());
        return success ? CommonResult.success(null) : CommonResult.failed("购物车中不存在该商品");
    }

    @ApiOperation("清空当前用户购物车")
    @ResponseBody
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public CommonResult clear() {
        boolean success = omsShoppingCartService.clearCurrent();
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @ApiOperation("购物车汇总（总商品数量与总金额）")
    @ResponseBody
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public CommonResult<CartSummaryVO> summary() {
        CartSummaryVO vo = omsShoppingCartService.getSummary();
        return CommonResult.success(vo);
    }
}