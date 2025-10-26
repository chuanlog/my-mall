package com.macro.mall.tiny.modules.pms.controller;


import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;
import com.macro.mall.tiny.modules.pms.service.PmsProductCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品种类表 前端控制器
 * </p>
 *
 * @author cloog
 * @since 2025-10-23
 */
@Controller
@Api(tags = "PmsProductCategoryController")
@Tag(name = "PmsProductCategoryController",description = "商品分类管理")
@RequestMapping("/pms/productCategory")

public class PmsProductCategoryController {

    @Autowired
    private PmsProductCategoryService categoryService;

    @ApiOperation("创建商品分类")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody PmsProductCategory category) {
        boolean success = categoryService.create(category);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("更新商品分类")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody PmsProductCategory category) {
        category.setId(id);
        boolean success = categoryService.updateById(category);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除商品分类")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        boolean success = categoryService.delete(ids);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取所有商品分类")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProductCategory>> listAll() {
        List<PmsProductCategory> list = categoryService.list();
        return CommonResult.success(list);
    }

    @ApiOperation("根据名称分页获取商品分类列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProductCategory>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<PmsProductCategory> page = categoryService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取商品分类详情")
    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsProductCategory> item(@PathVariable Long id) {
        PmsProductCategory category = categoryService.getById(id);
        return CommonResult.success(category);
    }

    @ApiOperation("修改商品分类状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam("status") Boolean status) {
        PmsProductCategory update = new PmsProductCategory();
        update.setId(id);
        update.setStatus(status);
        boolean success = categoryService.updateById(update);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("上传商品分类图片")
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
            String url = categoryService.uploadImage(id, file);
            return CommonResult.success(url, "图片上传成功");
        } catch (Exception e) {
            return CommonResult.failed("图片上传失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新商品分类图片")
    @RequestMapping(value = "/image/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateImage(@PathVariable Long id,
                                                                   @RequestParam("imageUrl") String imageUrl) {
        try {
            boolean success = categoryService.updateImage(id, imageUrl);
            if (success) {
                return CommonResult.success(null, "图片更新成功");
            }
            return CommonResult.failed("图片更新失败");
        } catch (Exception e) {
            return CommonResult.failed("图片更新失败: " + e.getMessage());
        }
    }
}

