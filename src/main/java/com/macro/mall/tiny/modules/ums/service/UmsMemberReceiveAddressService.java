package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * 用户收货地址 Service
 */
public interface UmsMemberReceiveAddressService extends IService<UmsMemberReceiveAddress> {

    boolean create(UmsMemberReceiveAddress address);

    boolean update(Long id, UmsMemberReceiveAddress address);

    boolean delete(Long id);

    List<UmsMemberReceiveAddress> listByMemberId(Long memberId);

    Page<UmsMemberReceiveAddress> list(Long memberId, Integer pageSize, Integer pageNum);

    boolean setDefault(Long id);
}