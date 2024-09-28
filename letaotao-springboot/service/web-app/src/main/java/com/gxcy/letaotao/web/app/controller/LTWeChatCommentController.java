package com.gxcy.letaotao.web.app.controller;


import com.gxcy.letaotao.common.annotations.MethodExporter;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.service.LTWeChatCommentService;
import com.gxcy.letaotao.web.app.vo.LTWechatCommentQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatCommentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "留言/评论控制器")
@RestController
@RequestMapping("/api/wx_comments")
public class LTWeChatCommentController {

    @Resource
    private LTWeChatCommentService commentService;

    /**
     * 查询留言/评论列表树列表
     *
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询留言/评论列表树列表")
    @MethodExporter
    public Result<?> getCommentTreeList(LTWechatCommentQueryVo commentVO) {
        // 调用查询评论列表树列表方法
        List<LTWechatCommentVo> commentTrees = commentService.getCommentTree(commentVO);
        // 返回数据
        return Result.ok(commentTrees);
    }

    @PostMapping("")
    @Operation(summary = "添加留言/评论")
    public Result<?> save(@Validated @RequestBody LTWechatCommentVo commentVo) {
        LTWechatCommentVo ltWechatCommentVo = commentService.add(commentVo);
        return ltWechatCommentVo != null ? Result.ok(ltWechatCommentVo).message("添加成功") : Result.error().message("添加失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除留言/评论")
    public Result<?> delete(@PathVariable Integer id) {
        if (commentService.deleteById(id)) {
            return Result.ok().message("删除成功");
        }
        return Result.error().message("删除失败");
    }

}
