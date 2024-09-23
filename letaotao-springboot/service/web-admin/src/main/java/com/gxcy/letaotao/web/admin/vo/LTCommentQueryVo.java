package com.gxcy.letaotao.web.admin.vo;


import com.gxcy.letaotao.common.enums.LTCommentType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class LTCommentQueryVo {

    private Long pageNo = 1L; // 当前页码
    private Long pageSize = 10L; // 每页显示数量

    private Long userId; // 用户编号

    /**
     * 留言/评论内容
     */
    private String content;

    /**
     * 留言/评论时间
     */
    private LocalDateTime commentTime;

    /**
     * 留言/评论类型（1：留言，2：评论）
     */
    private LTCommentType type;

    private String nickName; // 用户昵称
    private Date startTime; // 开始时间
    private Date endTime; // 结束时间
    private String targetName; //商品/帖子名称

}
