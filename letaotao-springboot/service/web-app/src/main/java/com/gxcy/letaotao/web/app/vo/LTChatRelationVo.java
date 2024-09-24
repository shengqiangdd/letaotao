package com.gxcy.letaotao.web.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author csq
 * @Date 2024/9/9 22:58
 */
@Data
@NoArgsConstructor
public class LTChatRelationVo implements Serializable {

    /**
     * 聊天关系唯一标识符
     */
    private Integer id;
    /**
     * 买家编号
     */
    private Long buyerId;
    /**
     * 卖家编号
     */
    private Long sellerId;
    /**
     * 商品编号
     */
    private Integer productId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private LTUserVo seller;


    private LTUserVo buyer;


    private LTWechatMessageVo message;


    private LTWechatProductVo product;
}
