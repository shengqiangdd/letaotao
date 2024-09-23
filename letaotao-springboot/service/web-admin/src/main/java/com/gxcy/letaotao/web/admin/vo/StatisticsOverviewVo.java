package com.gxcy.letaotao.web.admin.vo;

import lombok.Data;

@Data
public class StatisticsOverviewVo {

    private Long yearlyUserCount;
    private Long monthlyUserCount;

    private Long yearlyProductCount;
    private Long monthlyProductCount;

    private Long yearlyProductCommentCount;
    private Long monthlyProductCommentCount;

    private Long yearlyOrderCount;
    private Long monthlyOrderCount;

    private Long yearlyPostCount;
    private Long monthlyPostCount;

    private Long yearlyPostCommentCount;
    private Long monthlyPostCommentCount;

}
