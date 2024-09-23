package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.LTPostStatus;
import lombok.Data;

@Data
public class LTWechatPostQueryVo {

    private Long pageNo = 1L; // 当前页码

    private Long pageSize = 10L; // 每页显示数量

    private String title;

    private String content;

    private LTPostStatus status;

    private Long userId;

    private Long currentUserId;

    private String label;

    private Long followerId;
}
