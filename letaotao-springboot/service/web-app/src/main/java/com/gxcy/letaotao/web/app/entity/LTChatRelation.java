package com.gxcy.letaotao.web.app.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天关系表
 */
@Data
@TableName("lt_chat_relation")
@NoArgsConstructor
public class LTChatRelation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}