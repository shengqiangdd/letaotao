<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form
      :model="orderSearchModel"
      ref="searchForm"
      label-width="80px"
      :inline="false"
      size="small"
    >
      <el-row>
        <el-col :span="6">
          <el-form-item>
            <el-input
              v-model="orderSearchModel.orderNum"
              placeholder="订单号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="订单状态">
            <dict-select
              dictCode="orderStatus"
              v-model="orderSearchModel.status"
              placeholder="订单状态"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-input
              v-model="orderSearchModel.productName"
              placeholder="商品名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="6">
          <el-form-item>
            <el-button
              :icon="Search"
              type="primary"
              @click="search(pageNo, pageSize)"
            >
              查询
            </el-button>
            <el-button :icon="Delete" @click="resetValue()">重置</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <!-- 订单表格数据 -->
    <el-table
      :height="tableHeight"
      :data="orderList"
      border
      stripe
      style="width: 100%; margin-bottom: 10px"
    >
      <el-table-column prop="orderNum" label="订单号" />
      <el-table-column prop="productName" label="商品名称" />
      <el-table-column prop="price" label="商品价格" />
      <el-table-column prop="addressName" label="收货地址" />
      <el-table-column prop="status" label="订单状态">
        <template #default="scope">
          <span v-if="scope.row.status === 1">待付款</span>
          <span v-if="scope.row.status === 2">待发货</span>
          <span v-if="scope.row.status === 3">待收货</span>
          <span v-if="scope.row.status === 4">已完成</span>
          <span v-if="scope.row.status === 5">已取消</span>
        </template>
      </el-table-column>
      <el-table-column prop="buyerName" label="买家昵称" />
      <el-table-column prop="sellerName" label="卖家昵称" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column align="center" width="100" label="操作">
        <template #default="scope">
          <el-button
            :icon="Delete"
            type="danger"
            size="default"
            @click="handleDelete(scope.row)"
            v-if="hasAuth('ltt:order:delete') && scope.row.status === 5"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页工具栏 -->
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNo"
      :page-sizes="[10, 20, 30, 40, 50]"
      :page-size="10"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    />
  </el-main>
</template>

<script setup lang="ts">
import orderApi from "@/api/order";
import type { FormInstance } from "element-plus";
import { Delete, Search } from "@element-plus/icons-vue";
import { hasAuth } from "@/plugins/permission";
import { confirm as myConfirm, resetForm } from "@/utils";

const orderSearchModel = reactive({
  orderNum: "",
  productName: "",
  status: "",
});
const orderList = ref<any[]>([]);
const tableHeight = ref(0);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);

const searchForm = ref<FormInstance>();

const search = async (pageNo = 1, pageSize = 10) => {
  const params = {
    pageNo: pageNo,
    pageSize: pageSize,
    ...orderSearchModel,
  };
  const { records, total: totalVal } = await orderApi.getOrderList(params);
  orderList.value = records;
  total.value = totalVal;
};

watchEffect(() => {
  tableHeight.value = window.innerHeight - 220;
});

onMounted(() => {
  search();
});

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  search(pageNo.value, size);
};

const handleCurrentChange = (page: number) => {
  pageNo.value = page;
  search(page, pageSize.value);
};

const resetValue = () => {
  resetForm(searchForm.value, orderSearchModel);
  search();
};

const handleDelete = async (row: any) => {
  const confirm = await myConfirm(`确定要删除订单【${row.orderNum}】吗?`);
  if (confirm) {
    const { message } = await orderApi.deleteOrderById(row.id);
    ElMessage.success(message);
    search(pageNo.value, pageSize.value);
  }
};
</script>

<style lang="scss"></style>
