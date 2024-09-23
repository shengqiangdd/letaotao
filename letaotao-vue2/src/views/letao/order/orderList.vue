<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form
      :model="orderSearchModel"
      ref="searchForm"
      label-width="80px"
      :inline="true"
      size="small"
    >
      <el-form-item>
        <el-input
          v-model="orderSearchModel.orderNum"
          placeholder="订单号"
        ></el-input>
      </el-form-item>
      <el-form-item label="订单状态">
        <dict-select
          dictCode="orderStatus"
          v-model="orderSearchModel.status"
          placeholder="订单状态"
        ></dict-select>
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="orderSearchModel.productName"
          placeholder="商品名称"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          type="primary"
          @click="search(pageNo, pageSize)"
          >查询</el-button
        >
        <el-button icon="el-icon-delete" @click="resetValue()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 订单表格数据 -->
    <el-table
      :height="tableHeight"
      :data="orderList"
      border
      stripe
      style="width: 100%; margin-bottom: 10px"
    >
      <el-table-column prop="orderNum" label="订单号"></el-table-column>
      <el-table-column prop="productName" label="商品名称"></el-table-column>
      <el-table-column prop="price" label="商品价格"></el-table-column>
      <el-table-column prop="addressName" label="收货地址"></el-table-column>
      <el-table-column prop="status" label="订单状态">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 1">待付款</span>
          <span v-if="scope.row.status === 2">待发货</span>
          <span v-if="scope.row.status === 3">待收货</span>
          <span v-if="scope.row.status === 4">已完成</span>
          <span v-if="scope.row.status === 5">已取消</span>
        </template>
      </el-table-column>
      <el-table-column prop="buyerName" label="买家昵称"></el-table-column>
      <el-table-column prop="sellerName" label="卖家昵称"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column align="center" width="100" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-delete"
            type="danger"
            size="mini"
            @click="handleDelete(scope.row)"
            v-if="hasPermission('ltt:order:delete') && scope.row.status === 5"
            >删除</el-button
          >
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
    >
    </el-pagination>
  </el-main>
</template>
<script>
import orderApi from "@/api/order";
import DictSelect from "@/components/system/DictSelect";
export default {
  name: "orderList",
  components: { DictSelect },
  data() {
    return {
      // 查询条件对象
      orderSearchModel: {
        orderNum: "",
        productName: "",
        status: "",
      },
      orderList: [], // 订单列表
      tableHeight: 0, // 表格高度
      pageNo: 1, // 当前页码
      pageSize: 10, // 每页显示数量
      total: 0, // 总数量
    };
  },
  mounted() {
    // 获取订单列表
    this.search();
    // 表格高度
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  methods: {
    /** * 查询订单列表 */
    async search(pageNo = 1, pageSize = 10) {
      let params = {
        pageNo: pageNo,
        pageSize: pageSize,
        ...this.orderSearchModel,
      };
      // 发送查询请求
      let res = await orderApi.getOrderList(params);
      if (res.success) {
        this.orderList = res.data.records;
        this.total = res.data.total;
      } else {
        this.$message.error(res.message);
      }
    },
    /** * 每页数量发生变化时触发该事件 */
    handleSizeChange(size) {
      this.pageSize = size;
      // 将每页显示的数量交给成员变量
      this.search(this.pageNo, size);
    },
    /** * 当页码发生变化时触发该事件 */
    handleCurrentChange(page) {
      this.pageNo = page; // 调用查询方法
      this.search(page, this.pageSize);
    },
    /** * 重置查询条件 */
    resetValue() {
      // 清空查询条件
      this.$resetForm("searchForm", this.orderSearchModel);
      // 重新查询
      this.search();
    },
    /** * 删除 */
    async handleDelete(row) {
      let confirm = await this.$myconfirm("确定要删除该数据吗?");
      if (confirm) {
        // 封装条件
        let params = { id: row.id };
        // 发送删除请求
        let res = await orderApi.deleteOrderById(params);
        // 判断是否成功
        if (res.success) {
          this.$message.success(res.message);
          // 刷新
          this.search(this.pageNo, this.pageSize);
        } else {
          this.$message.error(res.message);
        }
      }
    },
  },
};
</script>
<style lang="scss"></style>
