<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form
      :model="messageSearchModel"
      ref="searchForm"
      label-width="80px"
      :inline="false"
      size="small"
    >
      <el-row>
        <el-col :span="6">
          <el-form-item>
            <el-input
              v-model="messageSearchModel.nickName"
              placeholder="用户昵称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-input
              v-model="messageSearchModel.content"
              placeholder="通知/消息内容"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-select
              v-model="messageSearchModel.type"
              placeholder="通知/消息类型"
            >
              <el-option label="全部" value="" />
              <el-option label="通知" value="1" />
              <el-option label="消息" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
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
      </el-row>
    </el-form>
    <!-- 通知/消息表格数据 -->
    <el-table
      :height="tableHeight"
      :data="messageList"
      border
      stripe
      style="width: 100%; margin-bottom: 10px"
    >
      <el-table-column prop="id" label="编号" />
      <el-table-column prop="content" label="内容" />
      <el-table-column prop="type" label="类型">
        <template #default="scope">
          <span>{{ scope.row.type == 1 ? "通知" : "消息" }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="senderName" label="发送者" />
      <el-table-column prop="receiverName" label="接收者" />
      <el-table-column prop="sendTime" label="发送时间" />
      <el-table-column align="center" width="100" label="操作">
        <template #default="scope">
          <el-button
            :icon="Delete"
            type="danger"
            size="default"
            @click="handleDelete(scope.row)"
            v-if="hasAuth('ltt:message:delete')"
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
import messageApi from "@/api/message";
import type { FormInstance } from "element-plus";
import { Delete, Search } from "@element-plus/icons-vue";
import { hasAuth } from "@/plugins/permission";
import { confirm as myConfirm, resetForm } from "@/utils";

const messageSearchModel = reactive({
  content: "",
  type: "",
  nickName: "",
});
const messageList = ref<any[]>([]);
const tableHeight = ref(0);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);

const searchForm = ref<FormInstance>();

const search = async (pageNo = 1, pageSize = 10) => {
  const params = {
    pageNo: pageNo,
    pageSize: pageSize,
    ...messageSearchModel,
  };
  const { records, total: totalVal } = await messageApi.getMessageList(params);
  messageList.value = records;
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
  resetForm(searchForm.value, messageSearchModel);
  search();
};

const handleDelete = async (row: any) => {
  const confirm = await myConfirm("确定要删除该数据吗?");
  if (confirm) {
    const { message } = await messageApi.deleteMessageById(row.id);
    ElMessage.success(message);
    search(pageNo.value, pageSize.value);
  }
};
</script>

<style lang="scss"></style>
