package com.macro.mall.tiny.common.utils;

import com.macro.mall.tiny.domain.AdminUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    /**
     * 零查表方式获取当前登录用户ID：
     * 直接从SecurityContext的principal中读取，若未登录返回null
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof AdminUserDetails) {
            return ((AdminUserDetails) principal).getId();
        }
        return null;
    }
}