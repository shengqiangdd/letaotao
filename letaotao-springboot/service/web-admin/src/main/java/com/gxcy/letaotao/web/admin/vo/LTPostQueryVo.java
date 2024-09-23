package com.gxcy.letaotao.web.admin.vo;

import com.gxcy.letaotao.common.enums.LTOrderStatus;
import lombok.Data;

@Data
public class LTPostQueryVo {

    private Long pageNo = 1L; // 当前页码
    private Long pageSize = 10L; // 每页显示数量

    private String[] betweenTime; // 开始-结束时间

    private String label;
    private String nickName;
    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    private LTOrderStatus status;
}
