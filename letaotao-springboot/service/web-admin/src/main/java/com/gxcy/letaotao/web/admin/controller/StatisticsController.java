package com.gxcy.letaotao.web.admin.controller;

import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.StatisticsService;
import com.gxcy.letaotao.web.admin.vo.StatisticsOverviewVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "统计控制器")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    // 获取年统计数据
    @GetMapping("/overview/{year}/{month}")
    public Result<?> getYearlyStatistics(@PathVariable Integer year, @PathVariable Integer month) {
        // 创建一个响应对象来包含所有统计数据
        StatisticsOverviewVo overview = new StatisticsOverviewVo();

        // 获取并设置年度统计数据
        overview.setYearlyUserCount(statisticsService.countRegisteredUsers(year, null));
        overview.setYearlyProductCount(statisticsService.countProductsOnSale(year, null));
        overview.setYearlyProductCommentCount(statisticsService.countProductComments(year, null));
        overview.setYearlyOrderCount(statisticsService.countOrders(year, null));
        overview.setYearlyPostCount(statisticsService.countPosts(year, null));
        overview.setYearlyPostCommentCount(statisticsService.countPostComments(year, null));

        // 获取并设置月度统计数据
        overview.setMonthlyUserCount(statisticsService.countRegisteredUsers(year, month));
        overview.setMonthlyProductCount(statisticsService.countProductsOnSale(year, month));
        overview.setMonthlyProductCommentCount(statisticsService.countProductComments(year, month));
        overview.setMonthlyOrderCount(statisticsService.countOrders(year, month));
        overview.setMonthlyPostCount(statisticsService.countPosts(year, month));
        overview.setMonthlyPostCommentCount(statisticsService.countPostComments(year, month));

        // 返回统计结果
        return Result.ok(overview);
    }

}
