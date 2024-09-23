package com.gxcy.letaotao.web.app.controller;

import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.dto.LTLikeDTO;
import com.gxcy.letaotao.web.app.service.LTWeChatLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "点赞控制器")
@RestController
@RequestMapping("/api/wx_likes")
public class LTWeChatLikeController {

    @Resource
    private LTWeChatLikeService ltWeChatLikeService;

    /**
     * 点赞或取消点赞
     *
     * @param ltLike
     * @return
     */
    @PostMapping("")
    @Operation(summary = "点赞或取消点赞")
    public Result<?> addOrUpdate(@RequestBody LTLikeDTO ltLike) {
        int status = ltWeChatLikeService.addOrUpdate(ltLike);
        return Result.ok(status).message("点赞操作成功");
    }

}