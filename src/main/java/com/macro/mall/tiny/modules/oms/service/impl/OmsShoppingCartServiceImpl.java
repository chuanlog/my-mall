package com.macro.mall.tiny.modules.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.utils.UserUtil;
import com.macro.mall.tiny.modules.oms.dto.AddShoppingCartDTO;
import com.macro.mall.tiny.modules.oms.mapper.OmsShoppingCartMapper;
import com.macro.mall.tiny.modules.oms.model.OmsShoppingCart;
import com.macro.mall.tiny.modules.oms.service.OmsShoppingCartService;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductMapper;
import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author cloog
 * @since 2025-10-26
 */
@Service
public class OmsShoppingCartServiceImpl extends ServiceImpl<OmsShoppingCartMapper, OmsShoppingCart> implements OmsShoppingCartService {

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Autowired
    private UserUtil userUtil;


    @Override
    public boolean add(AddShoppingCartDTO addShoppingCartDTO) {
        // 查找到商品
        PmsProduct pmsProduct = pmsProductMapper.selectById(addShoppingCartDTO.getProductId());
        // 获取当前用户
        Long adminId = userUtil.getCurrentUserId();
        // 构建购物车
        OmsShoppingCart omsShoppingCart = new OmsShoppingCart();
        omsShoppingCart.setMemberId(adminId);
        omsShoppingCart.setProductId(addShoppingCartDTO.getProductId());
        omsShoppingCart.setProductName(pmsProduct.getName());
        omsShoppingCart.setProductImage(pmsProduct.getImage());
        omsShoppingCart.setProductPrice(pmsProduct.getPrice());
        omsShoppingCart.setQuantity(addShoppingCartDTO.getQuantity());
        omsShoppingCart.setCreateTime(new Date());
        return save(omsShoppingCart);

    }
}
