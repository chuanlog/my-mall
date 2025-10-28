package com.macro.mall.tiny.modules.ums.service;

import com.macro.mall.tiny.modules.ums.dto.UmsAdminParam;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;

/**
 * 普通用户管理Service
 * Created by assistant on 2025/1/1.
 */
public interface UmsCustomerService {
    /**
     * 用户注册功能
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 用户登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

    /**
     * 根据用户名获取用户信息
     */
    UmsAdmin getCustomerByUsername(String username);

    /**
     * 刷新token的功能
     * @param oldToken 旧的token
     */
    String refreshToken(String oldToken);
}