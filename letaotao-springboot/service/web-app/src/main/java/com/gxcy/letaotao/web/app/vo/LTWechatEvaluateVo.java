package com.gxcy.letaotao.web.app.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author csq
 * @Date 2024/9/9 23:05
 */
@Data
@NoArgsConstructor
public class LTWechatEvaluateVo implements Serializable {

    /**
     * 评价唯一标识
     */
    private Integer id;

    /**
     * 订单编号编号
     */
    @NotNull(message = "订单ID不能为空")
    private Integer orderId;

    /**
     * 用户编号
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 评价内容
     */
    @NotEmpty(message = "评价内容不能为空")
    private String content;

    /**
     * 是否好评
     */
    @NotNull(message = "是否好评不能为空")
    private BooleanStatus isFavor;

    /**
     * 评价时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    private String nickName;


    private String avatar;

    private String tag;
}
