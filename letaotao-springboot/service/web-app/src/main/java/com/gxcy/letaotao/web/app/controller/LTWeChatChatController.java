package com.gxcy.letaotao.web.app.controller;

import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.service.LTWeChatChatRelationService;
import com.gxcy.letaotao.web.app.service.LTWeChatMessageService;
import com.gxcy.letaotao.web.app.vo.LTChatRelationVo;
import com.gxcy.letaotao.web.app.vo.LTWechatMessageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "聊天控制器")
@RestController
@RequestMapping("/api/wx_chat")
public class LTWeChatChatController {
    @Resource
    private LTWeChatChatRelationService chatRelationService;
    @Resource
    private LTWeChatMessageService ltMessageService;

    @GetMapping("/relation")
    @Operation(summary = "获取聊天关系")
    public Result<?> getOrCreateChatRelation(LTChatRelationVo chatRelationVO) {
        // 根据商品id、买家id、卖家id查询聊天关系
        LTChatRelationVo chatRelation = chatRelationService
                .getOrCreateChatRelation(chatRelationVO.getBuyerId(),
                        chatRelationVO.getSellerId(), chatRelationVO.getProductId());
        return Result.ok(chatRelation);
    }

    @GetMapping("/messages/{relationId}")
    @Operation(summary = "获取聊天记录")
    public Result<?> getChatMessages(@PathVariable Integer relationId) {
        // 根据聊天关系id查询聊天记录
        List<LTWechatMessageVo> messages = chatRelationService.getChatMessages(relationId);
        return Result.ok(messages);
    }

    @PostMapping("/read")
    @Operation(summary = "批量更新消息为已读")
    public Result<?> batchRead(@RequestBody List<Integer> ids) {
        boolean update = ltMessageService.batchRead(ids);
        return update ? Result.ok().message("更新成功") : Result.error().message("更新失败");
    }

}