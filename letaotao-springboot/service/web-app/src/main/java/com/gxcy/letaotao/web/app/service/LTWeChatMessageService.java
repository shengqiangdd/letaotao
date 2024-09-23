package com.gxcy.letaotao.web.app.service;


import com.gxcy.letaotao.common.entity.LTMessage;
import com.gxcy.letaotao.web.app.vo.LTChatRelationVo;
import com.gxcy.letaotao.web.app.vo.LTWechatMessageVo;

import java.util.List;

public interface LTWeChatMessageService extends BaseService<LTMessage> {

    List<LTChatRelationVo> getChatMessageList();

    boolean batchRead(List<Integer> ids);

    LTWechatMessageVo findMessageById(Integer id);

    LTWechatMessageVo add(LTWechatMessageVo messageVo);

    boolean update(LTWechatMessageVo messageVo);

    boolean deleteById(Integer id);
}
