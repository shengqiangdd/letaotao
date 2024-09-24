package com.gxcy.letaotao.web.app.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.dto.LTWeChatPostDTO;
import com.gxcy.letaotao.web.app.service.LTWeChatPostService;
import com.gxcy.letaotao.web.app.vo.LTWechatPostQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatPostVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "帖子控制器")
@RestController
@RequestMapping("/api/wx_posts")
public class LTWeChatPostController {

    @Resource
    private LTWeChatPostService ltPostService;

    /**
     * 分页查询帖子列表
     *
     * @param postVO
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询帖子列表")
    public Result<?> getPostList(LTWechatPostQueryVo postVO) {
        // 创建分页对象
        IPage<LTWechatPostVo> page = new Page<>(postVO.getPageNo(), postVO.getPageSize());
        // 调用分页查询帖子列表方法
        ltPostService.findListByPage(page, postVO);
        // 返回数据
        return Result.ok(page);
    }

    @GetMapping("/list/{user}/page")
    @Operation(summary = "分页查询用户的帖子列表")
    public Result<?> getPostListByUserId(@PathVariable String user, LTWechatPostQueryVo postVO) {
        // 创建分页对象
        IPage<LTWechatPostVo> page = new Page<>(postVO.getPageNo(), postVO.getPageSize());
        // 调用分页查询帖子列表方法
        ltPostService.findListByUserId(page, postVO);
        // 返回数据
        return Result.ok(page);
    }

    @PostMapping("/save")
    @Operation(summary = "帖子保存草稿")
    public Result<?> save(@Validated @RequestBody LTWeChatPostDTO postDTO) {
        // 调用保存帖子方法
        int id = ltPostService.save(postDTO);
        return id > 0 ? Result.ok(id).message("保存成功") : Result.error().message("保存失败");
    }

    @PostMapping("/publish")
    @Operation(summary = "帖子发布")
    public Result<?> publish(@Validated @RequestBody LTWeChatPostDTO postDTO) {
        // 调用发布帖子方法
        int id = ltPostService.publish(postDTO);
        return id > 0 ? Result.ok(id).message("发布成功") : Result.error().message("发布失败");
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取帖子详情")
    public Result<?> get(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        LTWechatPostVo ltWechatPostVo = ltPostService.findRelation(id, userId);
        return ltWechatPostVo == null ? Result.error() : Result.ok(ltWechatPostVo);
    }

    @PutMapping("")
    @Operation(summary = "更新帖子")
    public Result<?> update(@Validated @RequestBody LTWeChatPostDTO postDTO) {
        boolean update = ltPostService.update(postDTO);
        return update ? Result.ok().message("修改成功") : Result.error().message("修改失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除帖子")
    public Result<?> delete(@PathVariable Integer id) {
        boolean delete = ltPostService.deleteById(id);
        return delete ? Result.ok().message("删除成功") : Result.error().message("删除失败");
    }

}
