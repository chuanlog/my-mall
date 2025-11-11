package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.macro.mall.tiny.modules.pms.service.PmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
@Controller
@Api(tags = "PmsProductController")
@Tag(name = "PmsProductController", description = "商品表管理")
@RequestMapping("/pms/product")
public class PmsProductController {

    @Autowired
    private PmsProductService productService;

    @ApiOperation("创建商品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody PmsProduct product) {
        boolean success = productService.create(product);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("更新商品")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody PmsProduct product) {
        boolean success = productService.updateProduct(id, product);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除商品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        boolean success = productService.delete(ids);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据名称/分类/状态/价格区间分页获取商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProduct>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                     @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                     @RequestParam(value = "status", required = false) Boolean status,
                                                     @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
                                                     @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<PmsProduct> page = productService.list(keyword, categoryId, status, minPrice, maxPrice, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取商品详情")
    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsProduct> item(@PathVariable Long id) {
        PmsProduct product = productService.getProductById(id);
        return CommonResult.success(product);
    }

    @ApiOperation("修改商品状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam("status") Boolean status) {
        PmsProduct update = new PmsProduct();
        update.setId(id);
        update.setStatus(status);
        boolean success = productService.updateById(update);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据分类分页获取商品列表")
    @RequestMapping(value = "/listByCategory/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProduct>> listByCategory(@PathVariable Long categoryId,
                                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<PmsProduct> page = productService.list(null, categoryId, null, null, null, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取所有商品")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProduct>> listAll() {
        List<PmsProduct> list = productService.list();
        return CommonResult.success(list);
    }

    @ApiOperation("上传商品图片")
    @RequestMapping(value = "/image/upload/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> uploadImage(@PathVariable Long id,
                                            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return CommonResult.failed("请选择要上传的文件");
            }
            if (file.getSize() > 5 * 1024 * 1024) {
                return CommonResult.failed("文件大小不能超过5MB");
            }
            String url = productService.uploadImage(id, file);
            return CommonResult.success(url, "图片上传成功");
        } catch (Exception e) {
            return CommonResult.failed("图片上传失败: " + e.getMessage());
        }
    }


}

