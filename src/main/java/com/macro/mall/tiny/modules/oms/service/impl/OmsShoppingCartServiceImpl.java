package com.macro.mall.tiny.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.utils.UserUtil;
import com.macro.mall.tiny.modules.oms.dto.AddShoppingCartDTO;
import com.macro.mall.tiny.modules.oms.dto.CartSummaryVO;
import com.macro.mall.tiny.modules.oms.mapper.OmsShoppingCartMapper;
import com.macro.mall.tiny.modules.oms.model.OmsShoppingCart;
import com.macro.mall.tiny.modules.oms.service.OmsShoppingCartService;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductMapper;
import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        Long memberId = userUtil.getCurrentUserId();

        // 查询是否已存在该商品条目
        LambdaQueryWrapper<OmsShoppingCart> wrapper = new LambdaQueryWrapper<OmsShoppingCart>()
                .eq(OmsShoppingCart::getMemberId, memberId)
                .eq(OmsShoppingCart::getProductId, addShoppingCartDTO.getProductId());
        OmsShoppingCart existing = getOne(wrapper);

        if (existing != null) {
            // 已存在则累加数量
            existing.setQuantity(existing.getQuantity() + addShoppingCartDTO.getQuantity());
            return updateById(existing);
        }

        // 新增购物车条目
        OmsShoppingCart cart = new OmsShoppingCart();
        cart.setMemberId(memberId);
        cart.setProductId(addShoppingCartDTO.getProductId());
        cart.setProductName(pmsProduct.getName());
        cart.setProductImage(pmsProduct.getImage());
        cart.setProductPrice(pmsProduct.getPrice());
        cart.setQuantity(addShoppingCartDTO.getQuantity());
        cart.setCreateTime(new Date());
        return save(cart);

    }

    @Override
    public List<OmsShoppingCart> listCurrent() {
        Long memberId = userUtil.getCurrentUserId();
        LambdaQueryWrapper<OmsShoppingCart> wrapper = new LambdaQueryWrapper<OmsShoppingCart>()
                .eq(OmsShoppingCart::getMemberId, memberId);
        return list(wrapper);
    }

    @Override
    public boolean updateQuantity(Long productId, Integer quantity) {
        Long memberId = userUtil.getCurrentUserId();
        LambdaQueryWrapper<OmsShoppingCart> wrapper = new LambdaQueryWrapper<OmsShoppingCart>()
                .eq(OmsShoppingCart::getMemberId, memberId)
                .eq(OmsShoppingCart::getProductId, productId);
        OmsShoppingCart existing = getOne(wrapper);
        if (existing == null) {
            return false;
        }
        existing.setQuantity(quantity);
        return updateById(existing);
    }

    @Override
    public boolean removeByProductId(Long productId) {
        Long memberId = userUtil.getCurrentUserId();
        LambdaQueryWrapper<OmsShoppingCart> wrapper = new LambdaQueryWrapper<OmsShoppingCart>()
                .eq(OmsShoppingCart::getMemberId, memberId)
                .eq(OmsShoppingCart::getProductId, productId);
        return remove(wrapper);
    }

    @Override
    public boolean clearCurrent() {
        Long memberId = userUtil.getCurrentUserId();
        LambdaQueryWrapper<OmsShoppingCart> wrapper = new LambdaQueryWrapper<OmsShoppingCart>()
                .eq(OmsShoppingCart::getMemberId, memberId);
        return remove(wrapper);
    }

    @Override
    public CartSummaryVO getSummary() {
        List<OmsShoppingCart> carts = listCurrent();
        int totalQty = 0;
        BigDecimal totalAmt = BigDecimal.ZERO;
        for (OmsShoppingCart c : carts) {
            int q = c.getQuantity() == null ? 0 : c.getQuantity();
            totalQty += q;
            BigDecimal price = c.getProductPrice() == null ? BigDecimal.ZERO : c.getProductPrice();
            totalAmt = totalAmt.add(price.multiply(BigDecimal.valueOf(q)));
        }
        CartSummaryVO vo = new CartSummaryVO();
        vo.setTotalQuantity(totalQty);
        vo.setTotalAmount(totalAmt);
        return vo;
    }
}
