package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.dto.UmsAdminLoginParam;
import com.macro.mall.tiny.modules.ums.dto.UmsAdminParam;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import com.macro.mall.tiny.modules.ums.service.UmsCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 普通用户管理
 * Created by assistant on 2025/1/1.
 */
@Controller
@Api(tags = "UmsCustomerController")
@Tag(name = "UmsCustomerController", description = "普通用户管理")
@RequestMapping("/customer")
public class UmsCustomerController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UmsCustomerService customerService;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UmsAdmin> register(@Validated @RequestBody UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = customerService.register(umsAdminParam);
        if (umsAdmin == null) {
            return CommonResult.failed("用户名已存在");
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        String token = customerService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = customerService.refreshToken(token);
        if (refreshToken == null) {
            return CommonResult.failed("token已经过期！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getCustomerInfo(Principal principal) {
        if (principal == null) {
            return CommonResult.unauthorized(null);
        }
        String username = principal.getName();
        UmsAdmin umsAdmin = customerService.getCustomerByUsername(username);
        if (umsAdmin == null) {
            return CommonResult.failed("用户不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        data.put("nickName", umsAdmin.getNickName());
        data.put("email", umsAdmin.getEmail());
        data.put("icon", umsAdmin.getIcon());
        data.put("createTime", umsAdmin.getCreateTime());
        return CommonResult.success(data);
    }

    @ApiOperation(value = "用户登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult logout() {
        return CommonResult.success(null);
    }
}