package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.mapper.UmsMemberReceiveAddressMapper;
import com.macro.mall.tiny.modules.ums.model.UmsMemberReceiveAddress;
import com.macro.mall.tiny.modules.ums.service.UmsMemberReceiveAddressService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户收货地址 Service 实现
 */
@Service
public class UmsMemberReceiveAddressServiceImpl extends ServiceImpl<UmsMemberReceiveAddressMapper, UmsMemberReceiveAddress>
        implements UmsMemberReceiveAddressService {

    @Override
    public boolean create(UmsMemberReceiveAddress address) {
        address.setCreateTime(new Date());
        if (address.getDefaultStatus() != null && address.getDefaultStatus() == 1) {
            // 清除该用户其他默认地址
            clearMemberDefault(address.getMemberId());
        }
        return save(address);
    }

    @Override
    public boolean update(Long id, UmsMemberReceiveAddress address) {
        address.setId(id);
        if (address.getDefaultStatus() != null && address.getDefaultStatus() == 1) {
            clearMemberDefault(address.getMemberId());
        }
        return updateById(address);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public List<UmsMemberReceiveAddress> listByMemberId(Long memberId) {
        LambdaQueryWrapper<UmsMemberReceiveAddress> wrapper = new LambdaQueryWrapper<UmsMemberReceiveAddress>()
                .eq(UmsMemberReceiveAddress::getMemberId, memberId)
                .orderByDesc(UmsMemberReceiveAddress::getDefaultStatus)
                .orderByDesc(UmsMemberReceiveAddress::getCreateTime);
        return list(wrapper);
    }

    @Override
    public Page<UmsMemberReceiveAddress> list(Long memberId, Integer pageSize, Integer pageNum) {
        Page<UmsMemberReceiveAddress> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UmsMemberReceiveAddress> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(memberId != null, UmsMemberReceiveAddress::getMemberId, memberId)
                .orderByDesc(UmsMemberReceiveAddress::getDefaultStatus)
                .orderByDesc(UmsMemberReceiveAddress::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public boolean setDefault(Long id) {
        UmsMemberReceiveAddress address = getById(id);
        if (address == null) {
            return false;
        }
        // 清除默认
        clearMemberDefault(address.getMemberId());
        // 设置当前为默认
        address.setDefaultStatus(1);
        return updateById(address);
    }

    private void clearMemberDefault(Long memberId) {
        if (memberId == null) return;
        UmsMemberReceiveAddress reset = new UmsMemberReceiveAddress();
        reset.setDefaultStatus(0);
        QueryWrapper<UmsMemberReceiveAddress> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsMemberReceiveAddress::getMemberId, memberId);
        update(reset, wrapper);
    }
}