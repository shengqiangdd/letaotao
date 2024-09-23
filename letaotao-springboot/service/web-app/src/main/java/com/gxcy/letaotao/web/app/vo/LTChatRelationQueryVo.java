package com.gxcy.letaotao.web.app.vo;

import lombok.Data;

/**
 * 聊天关系
 */
@Data
public class LTChatRelationQueryVo {

    private Long buyerId; // 买家
    private Long sellerId; // 卖家
    private Integer productId; // 商品Id
}
