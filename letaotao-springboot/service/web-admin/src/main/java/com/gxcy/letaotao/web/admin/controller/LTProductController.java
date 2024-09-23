package com.gxcy.letaotao.web.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.LTProductService;
import com.gxcy.letaotao.web.admin.vo.LTProductQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTProductVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品控制器")
@RestController
@RequestMapping("/api/products")
public class LTProductController {

    @Resource
    private LTProductService ltProductService;

    /**
     * 分页查询商品列表
     *
     * @param productQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询商品列表")
    public Result<?> getProductList(LTProductQueryVo productQueryVo) {
        // 创建分页对象
        IPage<LTProductVo> page = new Page<>(productQueryVo.getPageNo(), productQueryVo.getPageSize());
        // 调用分页查询商品列表方法
        ltProductService.findListByPage(page, productQueryVo);
        // 返回数据
        return Result.ok(page);
    }

    /**
     * 根据id查询商品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询商品信息")
    public Result<?> getProductById(@PathVariable Integer id) {
        return Result.ok(ltProductService.findProductById(id));
    }

    /**
     * 添加商品
     *
     * @param productVo
     * @return
     */
    @PostMapping("")
    @Operation(summary = "添加商品")
    @PreAuthorize("hasAuthority('ltt:product:add')")
    public Result<?> add(@RequestBody LTProductVo productVo) {
        boolean add = ltProductService.add(productVo);
        return add ? Result.ok().message("商品添加成功")
                : Result.error().message("商品添加失败");
    }

    /**
     * 修改商品
     *
     * @param productVo
     * @return
     */
    @PutMapping("")
    @Operation(summary = "修改商品")
    @PreAuthorize("hasAuthority('ltt:product:edit')")
    public Result<?> update(@RequestBody LTProductVo productVo) {
        boolean update = ltProductService.update(productVo);
        return update ? Result.ok().message("商品修改成功")
                : Result.error().message("商品修改失败");
    }

    /**
     * 批量修改商品
     *
     * @param ltProductVos
     * @return
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改商品")
    @PreAuthorize("hasAuthority('ltt:product:edit')")
    public Result<?> batchUpdate(@RequestBody List<LTProductVo> ltProductVos) {
        boolean update = ltProductService.batchUpdate(ltProductVos);
        return update ? Result.ok().message("商品修改成功")
                : Result.error().message("商品修改失败");
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品")
    @PreAuthorize("hasAuthority('ltt:product:delete')")
    public Result<?> delete(@PathVariable Integer id) {
        boolean delete = ltProductService.deleteById(id);
        return delete ? Result.ok().message("商品删除成功")
                : Result.error().message("商品删除失败");
    }
}