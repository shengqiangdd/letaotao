<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form
      :model="commentSearchModel"
      ref="searchForm"
      label-width="80px"
      :inline="true"
      size="small"
    >
      <el-form-item>
        <el-input
          v-model="commentSearchModel.content"
          placeholder="内容"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="commentSearchModel.nickName"
          placeholder="用户昵称"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="commentSearchModel.type" placeholder="类型">
          <el-option label="全部" value=""></el-option>
          <el-option label="留言" value="1"></el-option>
          <el-option label="评论" value="2"></el-option>
        </el-select>
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
    <!-- 留言/评论表格数据 -->
    <el-table
      :height="tableHeight"
      :data="commentList"
      border
      stripe
      style="width: 100%; margin-bottom: 10px"
    >
      <el-table-column prop="id" label="编号"></el-table-column>
      <el-table-column prop="nickName" label="用户昵称"></el-table-column>
      <el-table-column prop="content" label="内容"></el-table-column>
      <el-table-column
        prop="targetName"
        label="留言/评论目标"
      ></el-table-column>
      <el-table-column prop="type" label="类型">
        <template slot-scope="scope">
          <span>{{ scope.row.type == 2 ? "评论" : "留言" }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="commentTime"
        label="时间"
      ></el-table-column>
      <el-table-column align="center" width="100" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-delete"
            type="danger"
            size="mini"
            @click="handleDelete(scope.row)"
            v-if="hasPermission('ltt:comment:delete')"
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
import commentApi from "@/api/comment";
export default {
  name: "commentList",
  data() {
    return {
      // 查询条件对象
      commentSearchModel: {
        content: "",
        nickName: "",
        type: ""
      },
      commentList: [], // 留言/评论列表
      tableHeight: 0, // 表格高度
      pageNo: 1, // 当前页码
      pageSize: 10, // 每页显示数量
      total: 0, // 总数量
    };
  },
  mounted() {
    // 获取留言/评论列表
    this.search();
    // 表格高度
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  methods: {
    /** * 查询留言/评论列表 */
    async search(pageNo = 1, pageSize = 10) {
      let params = {
        pageNo: pageNo,
        pageSize: pageSize,
        ...this.commentSearchModel,
      };
      // 发送查询请求
      let res = await commentApi.getCommentList(params);
      if (res.success) {
        this.commentList = res.data.records;
        this.total = res.data.total;
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
      this.pageNo = page;
      // 调用查询方法
      this.search(page, this.pageSize);
    },
    /** * 重置查询条件 */
    resetValue() {
      // 清空查询条件
      this.$resetForm("searchForm", this.commentSearchModel);
      this.search(); // 重新查询
    },
    /** * 删除 */
    async handleDelete(row) {
      let confirm = await this.$myconfirm("确定要删除该数据吗?");
      if (confirm) {
        // 封装条件
        let params = { id: row.id }; // 发送删除请求
        let res = await commentApi.deleteCommentById(params);
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
