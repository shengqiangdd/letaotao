<template>
  <el-main>
    <el-row :gutter="20">
      <el-col :span="24">
        <el-radio-group v-model="chartType" @change="initCharts">
          <el-radio-button value="pie">饼图</el-radio-button>
          <el-radio-button value="bar">柱状图</el-radio-button>
          <el-radio-button value="line">折线图</el-radio-button>
        </el-radio-group>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <div v-for="chart in charts" :key="chart.id">
        <el-col :span="8">
          <div :id="chart.id" style="width: 400px; height: 400px"></div>
        </el-col>
      </div>
    </el-row>
  </el-main>
</template>

<script setup>
import statisticsApi from "@/api/statistics";
import * as echarts from "echarts";

const yearlyStats = ref(null);
const monthlyStats = ref(null);
const year = ref(new Date().getFullYear());
const month = ref(new Date().getMonth() + 1); // 获取当前月份
const chartType = ref("pie"); // 当前图表类型
const charts = [
  {
    id: "userChart",
    name: "用户统计",
    value: "userCount",
  },
  {
    id: "productChart",
    name: "商品统计",
    value: "productCount",
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
    value: "postCommentCount",
  },
];
const chartsInstance = ref({}); // 存储已初始化的图表实例

const getStatistics = () => {
  statisticsApi
    .get(`/${year.value}/${month.value}`)
    .then((data) => {
      yearlyStats.value = {
        userCount: data.yearlyUserCount,
        productCount: data.yearlyProductCount,
        productCommentCount: data.yearlyProductCommentCount,
        orderCount: data.yearlyOrderCount,
        postCount: data.yearlyPostCount,
        postCommentCount: data.yearlyPostCommentCount,
      };
      monthlyStats.value = {
        userCount: data.monthlyUserCount,
        productCount: data.monthlyProductCount,
        productCommentCount: data.monthlyProductCommentCount,
        orderCount: data.monthlyOrderCount,
        postCount: data.monthlyPostCount,
        postCommentCount: data.monthlyPostCommentCount,
      };
      initCharts();
    })
    .catch((error) => {
      ElMessage.error("获取统计数据失败：" + error);
    });
};

const initCharts = () => {
  charts.forEach((chart) => {
    initChart(chart.id, chart.name, [
      { value: yearlyStats.value[chart.value], name: "年度" },
      { value: monthlyStats.value[chart.value], name: "月度" },
    ]);
  });
};

const initChart = (chartDomId, title, data) => {
  const chartDom = document.getElementById(chartDomId);
  if (chartsInstance.value[chartDomId]) {
    chartsInstance.value[chartDomId].dispose(); // 销毁之前的图表
  }
  const myChart = echarts.init(chartDom);
  chartsInstance.value[chartDomId] = myChart;
  const options = getChartOptions(title, data); // 获取图表配置
  if (options) {
    myChart.setOption(options);
  }
};

const getChartOptions = (title, data) => {
  const optionMap = {
    pie: getPieOption(title, data),
    bar: getBarOption(title, data),
    line: getLineOption(title, data),
  };
  return optionMap[chartType.value] || null;
};

const getPieOption = (title, data) => {
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
};

const getBarOption = (title, data) => {
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
};

const getLineOption = (title, data) => {
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
};

onMounted(() => {
  getStatistics();
});

// 在组件卸载之前销毁图表实例
onBeforeUnmount(() => {
  Object.values(chartsInstance.value).forEach((instance) => {
    instance.dispose();
  });
});
</script>
