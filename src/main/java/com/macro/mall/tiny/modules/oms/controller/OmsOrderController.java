package com.macro.mall.tiny.modules.oms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.oms.dto.PlaceOrderDTO;
import com.macro.mall.tiny.modules.oms.dto.PayOrderDTO;
import com.macro.mall.tiny.modules.oms.dto.CancelOrderDTO;
import com.macro.mall.tiny.modules.oms.dto.OrderDetailVO;
import com.macro.mall.tiny.modules.oms.model.OmsOrder;
import com.macro.mall.tiny.modules.oms.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("下单")
    @ResponseBody
    @PostMapping("/place")
    public CommonResult<OmsOrder> place(@RequestBody PlaceOrderDTO dto) {
        try {
            OmsOrder order = omsOrderService.placeOrder(dto.getAddressId(), dto.getNote());
            return CommonResult.success(order);
        } catch (IllegalArgumentException e) {
            return CommonResult.failed(e.getMessage());
        } catch (IllegalStateException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("下单失败");
        }
    }
    @ApiOperation("模拟支付")
    @ResponseBody
    @PostMapping("/pay")
    public CommonResult<OmsOrder> pay(@RequestBody PayOrderDTO dto) {
        try {
            if (dto.getOrderId() == null) {
                return CommonResult.failed("订单ID不能为空");
            }
            OmsOrder order = omsOrderService.simulatePay(dto.getOrderId());
            return CommonResult.success(order);
        } catch (IllegalArgumentException e) {
            return CommonResult.failed(e.getMessage());
        } catch (IllegalStateException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("支付失败");
        }
    }
    @ApiOperation("取消订单（未支付）")
    @ResponseBody
    @PostMapping("/cancel")
    public CommonResult cancel(@RequestBody CancelOrderDTO dto) {
        try {
            if (dto.getOrderId() == null) {
                return CommonResult.failed("订单ID不能为空");
            }
            boolean success = omsOrderService.cancel(dto.getOrderId());
            return success ? CommonResult.success(null) : CommonResult.failed("取消失败");
        } catch (IllegalArgumentException e) {
            return CommonResult.failed(e.getMessage());
        } catch (IllegalStateException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("取消失败");
        }
    }

    @ApiOperation("获取订单详情")
    @ResponseBody
    @GetMapping("/detail/{id}")
    public CommonResult<OrderDetailVO> detail(@PathVariable Long id) {
        try {
            OrderDetailVO vo = omsOrderService.getDetail(id);
            return CommonResult.success(vo);
        } catch (IllegalArgumentException e) {
            return CommonResult.failed(e.getMessage());
        } catch (IllegalStateException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询失败");
        }
    }

    @ApiOperation("我的订单列表（支持按状态筛选：0待付/1已付/4已关闭等）")
    @ResponseBody
    @GetMapping("/list")
    public CommonResult<CommonPage<OmsOrder>> list(@RequestParam(value = "status", required = false) Integer status,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<OmsOrder> page = omsOrderService.listMy(status, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }
}