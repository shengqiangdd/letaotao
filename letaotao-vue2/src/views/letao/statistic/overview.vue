<template>
  <el-main>
    <el-row :gutter="20">
      <el-col :span="24">
        <el-radio-group v-model="chartType" @change="initCharts">
          <el-radio-button label="pie">饼图</el-radio-button>
          <el-radio-button label="bar">柱状图</el-radio-button>
          <el-radio-button label="line">折线图</el-radio-button>
        </el-radio-group>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <template v-for="chart in charts">
        <el-col :span="8" :key="chart.id">
          <div :id="chart.id" style="height: 400px"></div>
        </el-col>
      </template>
    </el-row>
  </el-main>
</template>

<script>
import statisticsApi from "@/api/statistics";
import * as echarts from "echarts";

export default {
  data() {
    return {
      yearlyStats: null,
      monthlyStats: null,
      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1, // 获取当前月份
      chartType: "pie", // 当前图表类型
      charts: [
        // 图表ID和名称的映射
        {
          id: "userChart",
          name: "用户统计",
          value: "userCount"
        },
        {
          id: "productChart",
          name: "商品统计",
          value: "productCount"
        },
        {
          id: "productCommentChart",
          name: "商品留言统计",
          value: "productCommentCount",
        },
        {
          id: "orderChart",
          name: "订单统计",
          value: "orderCount",
        },
        {
          id: "postChart",
          name: "帖子统计",
          value: "postCount",
        },
        {
          id: "postCommentChart",
          name: "帖子评论统计",
          value: "postCommentCount"
        },
      ],
      chartsInstance: {}, // 存储已初始化的图表实例
    };
  },
  methods: {
    getStatistics() {
      statisticsApi
        .get(`/${this.year}/${this.month}`)
        .then((res) => {
          this.yearlyStats = {
            userCount: res.data.yearlyUserCount,
            productCount: res.data.yearlyProductCount,
            productCommentCount: res.data.yearlyProductCommentCount,
            orderCount: res.data.yearlyOrderCount,
            postCount: res.data.yearlyPostCount,
            postCommentCount: res.data.yearlyPostCommentCount,
          };
          this.monthlyStats = {
            userCount: res.data.monthlyUserCount,
            productCount: res.data.monthlyProductCount,
            productCommentCount: res.data.monthlyProductCommentCount,
            orderCount: res.data.monthlyOrderCount,
            postCount: res.data.monthlyPostCount,
            postCommentCount: res.data.monthlyPostCommentCount,
          };
          // 初始化ECharts图表
          this.initCharts();
        })
        .catch((error) => {
          console.error("Error fetching statistics:", error);
        });
    },
    initCharts() {
      this.charts.forEach((chart) => {
        this.initChart(chart.id, chart.name, [
          { value: this.yearlyStats[chart.value], name: "年度" },
          { value: this.monthlyStats[chart.value], name: "月度" },
        ]);
      });
    },
    initChart(chartDomId, title, data) {
      let chartDom = document.getElementById(chartDomId);
      if (this.chartsInstance[chartDomId]) {
        this.chartsInstance[chartDomId].dispose(); // 销毁之前的图表
      }
      let myChart = echarts.init(chartDom);
      this.chartsInstance[chartDomId] = myChart;
      let options = this.getChartOptions(title, data); // 获取图表配置
      if (options) {
        myChart.setOption(options);
      }
    },
    getChartOptions(title, data) {
      const optionMap = {
        pie: this.getPieOption(title, data),
        bar: this.getBarOption(title, data),
        line: this.getLineOption(title, data),
      };
      return optionMap[this.chartType] || null;
    },
    getPieOption(title, data) {
      return {
        title: {
          text: title,
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c} ({d}%)",
        },
        legend: {
          orient: "vertical",
          left: "left",
          data: data.map((d) => d.name),
        },
        series: [
          {
            name: title,
            type: "pie",
            radius: "55%",
            center: ["50%", "60%"],
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
          },
        ],
      };
    },
    getBarOption(title, data) {
      return {
        title: { text: title },
        tooltip: { trigger: "axis" },
        xAxis: {
          type: "category",
          data: data.map((d) => d.name),
        },
        yAxis: { type: "value" },
        series: [
          {
            data: data,
            type: "bar",
          },
        ],
      };
    },
    getLineOption(title, data) {
      return {
        title: { text: title },
        tooltip: { trigger: "axis" },
        xAxis: {
          type: "category",
          data: data.map((d) => d.name),
        },
        yAxis: { type: "value" },
        series: [
          {
            data: data,
            type: "line",
            smooth: true,
          },
        ],
      };
    },
  },
  mounted() {
    this.getStatistics();
  },
};
</script>
