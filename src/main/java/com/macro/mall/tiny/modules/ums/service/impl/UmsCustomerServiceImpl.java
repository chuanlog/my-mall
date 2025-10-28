package com.macro.mall.tiny.modules.ums.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.macro.mall.tiny.common.exception.Asserts;
import com.macro.mall.tiny.domain.AdminUserDetails;
import com.macro.mall.tiny.modules.ums.dto.UmsAdminParam;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminLoginLogMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper;
import com.macro.mall.tiny.modules.ums.model.*;
import com.macro.mall.tiny.modules.ums.service.UmsAdminRoleRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsCustomerService;
import com.macro.mall.tiny.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 普通用户管理Service实现类
 * Created by assistant on 2025/1/1.
 */
@Service
public class UmsCustomerServiceImpl implements UmsCustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsCustomerServiceImpl.class);
    
    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private UmsRoleMapper roleMapper;
    @Autowired
    private UmsResourceMapper resourceMapper;
    @Autowired
    private UmsAdminLoginLogMapper loginLogMapper;
    @Autowired
    private UmsAdminRoleRelationService adminRoleRelationService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        
        //查询是否有相同用户名的用户
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = adminMapper.selectList(wrapper);
        if (umsAdminList.size() > 0) {
            return null;
        }
        
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        adminMapper.insert(umsAdmin);
        
        // 为新注册用户分配默认角色（普通用户）
        if (umsAdmin.getId() != null) {
            // 使用roleMapper查询"普通用户"角色
            QueryWrapper<UmsRole> roleWrapper = new QueryWrapper<>();
            roleWrapper.lambda().eq(UmsRole::getName, "普通用户");
            UmsRole defaultRole = roleMapper.selectOne(roleWrapper);
            
            if (defaultRole != null) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(umsAdmin.getId());
                roleRelation.setRoleId(defaultRole.getId());
                adminRoleRelationService.save(roleRelation);
            }
        }
        
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                Asserts.fail("密码不正确");
            }
            if (!userDetails.isEnabled()) {
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public UmsAdmin getCustomerByUsername(String username) {
        if (StrUtil.isEmpty(username)) {
            return null;
        }
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, username);
        List<UmsAdmin> adminList = adminMapper.selectList(wrapper);
        if (adminList != null && adminList.size() > 0) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    /**
     * 添加登录记录
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        UmsAdmin admin = getCustomerByUsername(username);
        if (admin == null) return;
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogMapper.insert(loginLog);
    }

    /**
     * 根据用户名加载用户详情
     */
    public UserDetails loadUserByUsername(String username) {
        //获取用户信息
        UmsAdmin admin = getCustomerByUsername(username);
        if (admin != null) {
            List<UmsResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin, resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    /**
     * 获取用户资源列表
     */
    public List<UmsResource> getResourceList(Long adminId) {
        return resourceMapper.getResourceList(adminId);
    }
}