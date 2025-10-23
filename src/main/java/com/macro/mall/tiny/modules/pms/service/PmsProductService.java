package com.macro.mall.tiny.modules.pms.service;

import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.math.BigDecimal;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
public interface PmsProductService extends IService<PmsProduct> {
    boolean create(PmsProduct product);

    boolean delete(List<Long> ids);

    Page<PmsProduct> list(String keyword, Long categoryId, Boolean status, BigDecimal minPrice, BigDecimal maxPrice, Integer pageSize, Integer pageNum);

    /**
     * 上传商品图片
     * @param productId 商品ID
     * @param file 图片文件
     * @return 图片URL
     */
    String uploadImage(Long productId, org.springframework.web.multipart.MultipartFile file);

    /**
     * 更新商品图片
     * @param productId 商品ID
     * @param imageUrl 图片URL
     * @return 是否成功
     */
    boolean updateImage(Long productId, String imageUrl);
}
