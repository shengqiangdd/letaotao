package com.gxcy.letaotao.web.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.annotations.MethodExporter;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.LTCommentService;
import com.gxcy.letaotao.web.admin.vo.LTCommentQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTCommentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "留言/评论控制器")
@RestController
@RequestMapping("/api/comments")
public class LTCommentController {

    @Resource
    private LTCommentService ltCommentService;

    /**
     * 分页查询留言/评论列表
     *
     * @param commentQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询留言/评论列表")
    @MethodExporter
    public Result<?> getCommentList(LTCommentQueryVo commentQueryVo) {
        // 创建分页对象
        IPage<LTCommentVo> page = new Page<>(commentQueryVo.getPageNo(), commentQueryVo.getPageSize());
        // 调用分页查询评论列表方法
        ltCommentService.findListByPage(page, commentQueryVo);
        // 返回数据
        return Result.ok(page);
    }


    /**
     * 根据id查询留言/评论信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询留言/评论信息")
    public Result<?> getCommentById(@PathVariable Integer id) {
        return Result.ok(ltCommentService.findCommentById(id));
    }

    /**
     * 删除留言/评论
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除留言/评论")
    @PreAuthorize("hasAuthority('ltt:comment:delete')")
    public Result<?> delete(@PathVariable Integer id) {
        boolean delete = ltCommentService.deleteById(id);
        return delete ? Result.ok().message("留言/评论删除成功") : Result.error().message("留言/评论删除失败");
    }
}