<template>
  <el-container :style="{ height: containerHeight + 'px' }">
    <!-- 表格数据 -->
    <el-main>
      <!-- 查询条件 -->
      <el-form
        :model="searchModel"
        ref="searchForm"
        label-width="80px"
        :inline="true"
        size="small"
      >
        <el-form-item>
          <el-input v-model="searchModel.nickName" placeholder="请输入昵称" />
        </el-form-item>
        <dict-select dictCode="gender" v-model="searchModel.gender" placeholder="性别"></dict-select>
        <el-form-item>
          <el-button
            icon="el-icon-search"
            type="primary"
            @click="search(pageNo, pageSize)"
            >查询</el-button
          >
          <el-button icon="el-icon-delete" @click="resetValue()"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
      <!-- 用户表格数据 -->
      <el-table
        :height="tableHeight"
        :data="userList"
        border
        stripe
        style="width: 100%; margin-bottom: 10px"
      >
        <el-table-column prop="nickName" label="昵称" align="center"></el-table-column>
        <el-table-column prop="openId" label="微信openId"></el-table-column>
        <el-table-column prop="avatar" label="头像">
             <template slot-scope="scope">
          <el-avatar :size="100" :src="scope.row.avatar"></el-avatar>
        </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别" align="center">
            <template slot-scope="scope">
          <span v-if="scope.row.gender === 0">男</span>
          <span v-if="scope.row.gender === 1">女</span>
        </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="introduction" label="介绍"></el-table-column>
        <el-table-column prop="birthday" label="生日"></el-table-column>
        <el-table-column prop="createTime" label="注册时间"></el-table-column>
        <el-table-column align="center" width="100" label="操作">
          <template slot-scope="scope">
            <el-button
              icon="el-icon-delete"
              type="danger"
              size="mini"
              @click="handleDelete(scope.row)"
              v-if="hasPermission('letao:user:delete')"
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
  </el-container>
</template>

<script>
import DictSelect from '@/components/system/DictSelect';
//导入用户api脚本
import userApi from "@/api/lt-user";

export default {
  name: "ltUserList",
  components: {
    DictSelect
  },
  data() {
    return {
      containerHeight: 0, //容器高度
      //查询条件对象
      searchModel: {
        nickName: "",
        gender: "",
        pageNo: "",
        pageSize: "",
      },
      userList: [], //用户列表
      tableHeight: 0, //表格高度
      pageNo: 1, //当前页码
      pageSize: 10, //每页显示数量
      total: 0, //总数量
    };
  },
  methods: {
    /**
     * 查询用户列表
     */
    async search(pageNo = 1, pageSize = 10) {
      this.searchModel.pageNo = pageNo;
      this.searchModel.pageSize = pageSize;
      //发送查询请求
      let res = await userApi.getLtUserList(this.searchModel);
      if (res.success) {
        this.userList = res.data.records;
        this.total = res.data.total;
      }
    },
    /**
     * 当每页数量发生变化时触发该事件
     */
    handleSizeChange(size) {
      this.pageSize = size; //将每页显示的数量交给成员变量
      this.search(this.pageNo, size);
    },
    /**
     * 当页码发生变化时触发该事件
     */
    handleCurrentChange(page) {
      this.pageNo = page;
      //调用查询方法
      this.search(page, this.pageSize);
    },
    /**
     * 重置查询条件
     */
    resetValue() {
      //清空查询条件
      this.$resetForm("searchForm", this.searchModel);
      //重新查询
      this.search();
    },
    /**
     * 删除
     */
    async handleDelete(row) {
      let confirm = await this.$myconfirm("确定要删除该数据吗?");
      if (confirm) {
        //封装条件
        let params = { id: row.id };
        //发送删除请求
        let res = await userApi.deleteLtUser(params);
        //判断是否成功
        if (res.success) {
          this.$message.success(res.message);
          //刷新
          this.search(this.pageNo, this.pageSize);
        } else {
          this.$message.error(res.message);
        }
      }
    },
  },
  created() {
    //调用查询用户列表
    this.search();
  },
  mounted() {
    this.$nextTick(() => {
      //内容高度
      this.containerHeight = window.innerHeight - 85;
      //表格高度
      this.tableHeight = window.innerHeight - 220;
      //角色表格高度
      this.assignHeight = window.innerHeight - 350;
    });
  },
};
</script>

<style lang="scss">

</style>
