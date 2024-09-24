package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.web.app.entity.LTChatRelation;
import com.gxcy.letaotao.web.app.vo.LTChatRelationVo;
import com.gxcy.letaotao.web.app.vo.LTWechatMessageVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 聊天关系表 Mapper 接口
 */
public interface LTChatRelationMapper extends BaseMapper<LTChatRelation> {

    @Select("select `id`,`title`,`content`,message_type,sender_id,receiver_id," +
            "send_time,relation_id,is_image,is_read,is_order,order_id " +
            "from lt_message where relation_id = #{relationId}")
    List<LTWechatMessageVo> selectMessagesByRelationId(Integer relationId); // 通过关系id查询消息列表

    @Select("select `id`,buyer_id,seller_id,product_id,create_time from lt_chat_relation " +
            "where buyer_id = #{userId} or seller_id = #{userId}")
    List<LTChatRelationVo> findRelationListByUserId(Long userId); // 通过用户id查询聊天关系列表

    @Select("select `id` from lt_chat_relation where product_id = #{productId}")
    Integer selectRelationIdByProductId(Integer productId); // 通过商品id查询聊天关系id

    @Select("select `id`,buyer_id,seller_id,product_id,create_time from lt_chat_relation " +
            "where buyer_id = #{buyerId} and seller_id = #{sellerId} and product_id = #{productId}")
    LTChatRelationVo findRelation(Long buyerId, Long sellerId, Integer productId); // 通过用户id查询聊天关系

}
