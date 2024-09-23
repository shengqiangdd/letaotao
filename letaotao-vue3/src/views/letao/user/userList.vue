<template>
  <el-container :style="{ height: containerHeight + 'px' }">
    <!-- 表格数据 -->
    <el-main>
      <el-form
        :model="searchModel"
        ref="searchForm"
        label-width="80px"
        :inline="false"
        size="small"
      >
        <!-- 查询条件 -->
        <el-row>
          <el-col :span="6">
            <el-form-item>
              <el-input
                v-model="searchModel.nickName"
                placeholder="请输入昵称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <dict-select
                dictCode="gender"
                v-model="searchModel.gender"
                placeholder="性别"
              />
            </el-form-item>
          </el-col>
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
      <!-- 用户表格数据 -->
      <el-table
        :height="tableHeight"
        :data="userList"
        border
        stripe
        style="width: 100%; margin-bottom: 10px"
      >
        <el-table-column prop="nickName" label="昵称" align="center" />
        <el-table-column prop="openId" label="微信openId" />
        <el-table-column prop="avatar" label="头像">
          <template #default="{ row }">
            <el-avatar :size="100" :src="row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别" align="center">
          <template #default="{ row }">
            <span v-if="row.gender === 0">男</span>
            <span v-if="row.gender === 1">女</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="introduction" label="介绍" />
        <el-table-column prop="birthday" label="生日" />
        <el-table-column prop="createTime" label="注册时间" />
        <el-table-column align="center" width="100" label="操作">
          <template #default="scope">
            <el-button
              :icon="Delete"
              type="danger"
              size="default"
              @click="handleDelete(scope.row)"
              v-if="hasAuth('letao:user:delete')"
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
  </el-container>
</template>

<script setup lang="ts">
import userApi from "@/api/lt-user";
import { Delete, Search } from "@element-plus/icons-vue";
import { hasAuth } from "@/plugins/permission";
import { confirm as myConfirm, resetForm } from "@/utils";
import type { FormInstance } from "element-plus";

const searchModel = reactive({
  nickName: "",
  gender: "",
  pageNo: 1,
  pageSize: 10,
});
const userList = ref<any[]>([]);
const tableHeight = ref(0);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);
const containerHeight = ref(0);
const assignHeight = ref(0);

const searchForm = ref<FormInstance>();

const search = async (pageNo = 1, pageSize = 10) => {
  searchModel.pageNo = pageNo;
  searchModel.pageSize = pageSize;
  const { records, total: totalVal } = await userApi.getLtUserList(searchModel);
  userList.value = records;
  total.value = totalVal;
};

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  search(pageNo.value, size);
};

const handleCurrentChange = (page: number) => {
  pageNo.value = page;
  search(page, pageSize.value);
};

const resetValue = () => {
  resetForm(searchForm.value, searchModel);
  search();
};

const handleDelete = async (row: any) => {
  const confirm = await myConfirm(
    `确定要删除用户名为【${row.nickName}】的用户吗?`
  );
  if (confirm) {
    const { message } = await userApi.deleteLtUser(row.id);
    ElMessage.success(message);
    search(pageNo.value, pageSize.value);
  }
};

onMounted(() => {
  search();
  watchEffect(() => {
    containerHeight.value = window.innerHeight - 85;
    tableHeight.value = window.innerHeight - 220;
    assignHeight.value = window.innerHeight - 350;
  });
});
</script>

<style lang="scss"></style>
