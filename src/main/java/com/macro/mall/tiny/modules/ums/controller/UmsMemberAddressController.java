package com.macro.mall.tiny.modules.ums.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.utils.UserUtil;
import com.macro.mall.tiny.modules.ums.model.UmsMemberReceiveAddress;
import com.macro.mall.tiny.modules.ums.service.UmsMemberReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户收货地址管理
 */
@Controller
@Api(tags = "UmsMemberAddressController")
@Tag(name = "UmsMemberAddressController", description = "用户收货地址管理")
@RequestMapping("/member/address")
public class UmsMemberAddressController {

    @Autowired
    private UmsMemberReceiveAddressService addressService;

    @Autowired
    private UserUtil userUtil;

    @ApiOperation("新增收货地址")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UmsMemberReceiveAddress address) {
        // 默认使用当前登录用户ID
        if (address.getMemberId() == null) {
            address.setMemberId(userUtil.getCurrentUserId());
        }
        boolean success = addressService.create(address);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @ApiOperation("修改收货地址")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMemberReceiveAddress address) {
        if (address.getMemberId() == null) {
            address.setMemberId(userUtil.getCurrentUserId());
        }
        boolean success = addressService.update(id, address);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @ApiOperation("删除收货地址")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = addressService.delete(id);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @ApiOperation("根据ID获取收货地址详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsMemberReceiveAddress> getItem(@PathVariable Long id) {
        UmsMemberReceiveAddress address = addressService.getById(id);
        return CommonResult.success(address);
    }

    @ApiOperation("分页查询当前或指定用户的收货地址")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsMemberReceiveAddress>> list(@RequestParam(value = "memberId", required = false) Long memberId,
                                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Long finalMemberId = memberId != null ? memberId : userUtil.getCurrentUserId();
        Page<UmsMemberReceiveAddress> page = addressService.list(finalMemberId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("查询当前或指定用户的所有收货地址")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsMemberReceiveAddress>> listAll(@RequestParam(value = "memberId", required = false) Long memberId) {
        Long finalMemberId = memberId != null ? memberId : userUtil.getCurrentUserId();
        List<UmsMemberReceiveAddress> list = addressService.listByMemberId(finalMemberId);
        return CommonResult.success(list);
    }

    @ApiOperation("设置某地址为默认地址")
    @RequestMapping(value = "/updateDefault/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateDefault(@PathVariable Long id) {
        boolean success = addressService.setDefault(id);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }
}