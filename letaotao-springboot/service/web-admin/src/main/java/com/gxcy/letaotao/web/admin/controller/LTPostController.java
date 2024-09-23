package com.gxcy.letaotao.web.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.LTPostService;
import com.gxcy.letaotao.web.admin.vo.LTPostQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTPostVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "帖子控制器")
@RestController
@RequestMapping("/api/posts")
public class LTPostController {

    @Resource
    private LTPostService ltPostService;

    /**
     * 分页查询帖子列表
     *
     * @param postQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询帖子列表")
    public Result<?> getPostList(LTPostQueryVo postQueryVo) {
        // 创建分页对象
        IPage<LTPostVo> page = new Page<>(postQueryVo.getPageNo(), postQueryVo.getPageSize());
        // 调用分页查询帖子列表方法
        ltPostService.findListByPage(page, postQueryVo);
        // 返回数据
        return Result.ok(page);
    }


    /**
     * 根据id查询帖子信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询帖子信息")
    public Result<?> getPostById(@PathVariable Integer id) {
        return Result.ok(ltPostService.findPostById(id));
    }

    /**
     * 添加帖子
     *
     * @param postVo
     * @return
     */
    @PostMapping("")
    @Operation(summary = "添加帖子")
    @PreAuthorize("hasAuthority('ltt:post:add')")
    public Result<?> add(@RequestBody LTPostVo postVo) {
        boolean add = ltPostService.add(postVo);
        return add ? Result.ok().message("帖子添加成功") : Result.error().message("帖子添加失败");
    }

    /**
     * 修改帖子
     *
     * @param postVo
     * @return
     */
    @PutMapping("")
    @Operation(summary = "修改帖子")
    @PreAuthorize("hasAuthority('ltt:post:edit')")
    public Result<?> update(@RequestBody LTPostVo postVo) {
        boolean update = ltPostService.update(postVo);
        return update ? Result.ok().message("帖子修改成功") : Result.error().message("帖子修改失败");
    }

    /**
     * 批量修改帖子
     *
     * @param ltPostVos
     * @return
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改帖子")
    @PreAuthorize("hasAuthority('ltt:post:edit')")
    public Result<?> batchUpdate(@RequestBody List<LTPostVo> ltPostVos) {
        boolean delete = ltPostService.batchUpdate(ltPostVos);
        return delete ? Result.ok().message("帖子修改成功")
                : Result.error().message("帖子修改失败");
    }

    /**
     * 删除帖子
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除帖子")
    @PreAuthorize("hasAuthority('ltt:post:delete')")
    public Result<?> delete(@PathVariable Integer id) {
        boolean delete = ltPostService.deleteById(id);
        return delete ? Result.ok().message("帖子删除成功")
                : Result.error().message("帖子删除失败");
    }
}