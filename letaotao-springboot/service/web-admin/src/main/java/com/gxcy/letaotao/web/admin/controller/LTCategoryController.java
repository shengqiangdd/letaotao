package com.gxcy.letaotao.web.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.annotations.MethodExporter;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.LTCategoryService;
import com.gxcy.letaotao.web.admin.vo.LTCategoryQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTCategoryVo;
import com.gxcy.letaotao.web.admin.vo.SelectTree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "分类控制器")
@RestController
@RequestMapping("/api/categories")
public class LTCategoryController {

    @Resource
    private LTCategoryService ltCategoryService;

    /**
     * 分页查询分类列表
     *
     * @param categoryQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询分类列表")
    @MethodExporter
    public Result<?> getCategoryPageList(LTCategoryQueryVo categoryQueryVo) {
        // 创建分页对象
        IPage<LTCategoryVo> page = new Page<>(categoryQueryVo.getPageNo(), categoryQueryVo.getPageSize());
        // 调用分页查询分类列表方法
        ltCategoryService.findListByPage(page, categoryQueryVo);
        // 返回数据
        return Result.ok(page);
    }

    /**
     * 查询分类列表
     *
     * @param categoryQueryVo
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询分类列表")
    public Result<?> getCategoryList(LTCategoryQueryVo categoryQueryVo) {
        // 调用查询分类列表方法
        List<LTCategoryVo> LTCategoryList = ltCategoryService.findCategoryList(categoryQueryVo);
        // 返回数据
        return Result.ok(LTCategoryList);
    }

    /**
     * 查询分类树列表
     *
     * @return
     */
    @GetMapping("/tree")
    @Operation(summary = "查询分类树列表")
    public Result<?> getCategoryTreeList(@RequestParam(required = false) LTCategoryQueryVo categoryQueryVo) {
        // 调用查询分类树列表方法
        List<SelectTree> categoryTree = ltCategoryService.getCategoryTree(categoryQueryVo);
        // 返回数据
        return Result.ok(categoryTree);
    }

    /**
     * 根据id查询分类信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询分类信息")
    public Result<?> getCategoryById(@PathVariable Integer id) {
        return Result.ok(ltCategoryService.findCategoryById(id));
    }

    /**
     * 添加分类
     *
     * @param category
     * @return
     */
    @PostMapping("")
    @Operation(summary = "添加分类")
    @PreAuthorize("hasAuthority('ltt:category:add')")
    public Result<?> add(@RequestBody LTCategoryVo category) {
        // 调用添加分类方法
        boolean add = ltCategoryService.add(category);
        return add ? Result.ok().message("分类添加成功") : Result.error().message("分类添加失败");
    }

    /**
     * 修改分类
     *
     * @param category
     * @return
     */
    @PutMapping("")
    @Operation(summary = "修改分类")
    @PreAuthorize("hasAuthority('ltt:category:edit')")
    public Result<?> update(@RequestBody LTCategoryVo category) {
        // 调用修改分类方法
        boolean update = ltCategoryService.update(category);
        return update ? Result.ok().message("分类修改成功") : Result.error().message("分类修改失败");
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    @PreAuthorize("hasAuthority('ltt:category:delete')")
    public Result<?> delete(@PathVariable Integer id) {
        // 调用删除分类方法
        boolean delete = ltCategoryService.deleteById(id);
        return delete ? Result.ok().message("分类删除成功") : Result.error().message("分类删除失败");
    }

}