package com.gxcy.letaotao.web.app.controller;

import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.service.LTWeChatMessageService;
import com.gxcy.letaotao.web.app.vo.LTChatRelationVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "通知/消息控制器")
@RestController
@RequestMapping("/api/wx_messages")
public class LTWeChatMessageController {

    @Resource
    private LTWeChatMessageService ltMessageService;

    /**
     * 获取聊天列表
     *
     * @return
     */
    @GetMapping("/list")
    public Result<?> getChatMessages() {
        List<LTChatRelationVo> messageList = ltMessageService.getChatMessageList();
        return Result.ok(messageList);
    }

}