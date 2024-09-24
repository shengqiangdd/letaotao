package com.gxcy.letaotao.web.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 评价表
 */
@Data
@TableName("lt_evaluate")
@NoArgsConstructor
public class LTEvaluate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评价唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号编号
     */
    private Integer orderId;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 是否好评
     */
    private BooleanStatus isFavor;

    /**
     * 评价时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;

}
