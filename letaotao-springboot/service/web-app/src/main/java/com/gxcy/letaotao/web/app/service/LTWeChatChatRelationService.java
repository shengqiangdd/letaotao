package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.web.app.entity.LTChatRelation;
import com.gxcy.letaotao.web.app.vo.LTChatRelationVo;
import com.gxcy.letaotao.web.app.vo.LTWechatMessageVo;

import java.util.List;

/**
 * 聊天关系表 服务类
 */
public interface LTWeChatChatRelationService extends IService<LTChatRelation> {

    LTChatRelationVo getOrCreateChatRelation(Long buyerId, Long sellerId, Integer productId);

    List<LTWechatMessageVo> getChatMessages(Integer relationId);

    List<LTChatRelationVo> findRelationListByUserId(Long userId); // 通过用户id查询聊天关系列表

    int selectRelationIdByProductId(Integer productId); // 通过商品id查询聊天关系id

    boolean add(LTChatRelationVo chatRelationVo);

    boolean deleteById(Integer id);

    void deleteCatchById(Integer id);

    public void deleteByProductId(Integer productId);

}
