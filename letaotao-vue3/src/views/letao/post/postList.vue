<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form
      :model="postSearchModel"
      ref="searchForm"
      label-width="80px"
      :inline="false"
      size="small"
    >
      <el-row>
        <el-col :span="6">
          <el-form-item label="发布者">
            <el-input v-model="postSearchModel.nickName" placeholder="发布者" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="标题">
            <el-input v-model="postSearchModel.title" placeholder="标题" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="帖子内容">
            <el-input
              v-model="postSearchModel.content"
              placeholder="帖子内容"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="6">
          <el-form-item label="发帖时间" prop="betweenTime">
            <el-date-picker
              v-model="postSearchModel.betweenTime"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD HH:mm:ss"
              unlink-panels
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="帖子状态">
            <dict-select
              dictCode="postStatus"
              v-model="postSearchModel.status"
              placeholder="帖子状态"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="18">
          <el-form-item>
            <el-button
              :icon="Search"
              type="primary"
              @click="search(pageNo, pageSize)"
            >
              查询
            </el-button>
            <el-button :icon="Delete" @click="resetValue()">重置</el-button>
            <el-button
              :icon="Check"
              size="default"
              type="success"
              @click="batchOpertor(1)"
              v-if="hasAuth('ltt:post:audit')"
            >
              批量通过
            </el-button>
            <el-button
              :icon="SoldOut"
              size="default"
              type="danger"
              @click="batchOpertor(3)"
              v-if="hasAuth('ltt:post:out')"
            >
              批量不通过
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <!-- 帖子表格数据 -->
    <el-table
      ref="postTable"
      :height="tableHeight"
      :data="postList"
      border
      stripe
      @select-all="handelSelectAll"
      style="width: 100%; margin-bottom: 10px"
    >
      <el-table-column type="selection" prop="check" width="55" align="center">
        <template #default="scope">
          <el-checkbox
            v-model="scope.row.check"
            :checked="scope.row.check"
            @change="handelItemCheckChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="id" label="帖子编号" width="100" align="center" />
      <el-table-column
        prop="nickName"
        label="发布者"
        width="100"
        align="center"
      />
      <el-table-column prop="title" label="标题" width="150" align="center">
        <template #default="scope">
          <el-tooltip
            effect="dark"
            :content="scope.row.title"
            placement="top-start"
          >
            <span>{{ scope.row.title }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        prop="content"
        label="帖子内容"
        width="150"
        align="center"
      >
        <template #default="scope">
          <el-tooltip
            effect="dark"
            :content="scope.row.content"
            placement="top-start"
          >
            <span>{{ scope.row.content }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        prop="imageUrl"
        label="帖子图片"
        width="80"
        align="center"
      >
        <template #default="scope">
          <el-button size="default" @click="handlePreview(scope.row)">
            预览
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="帖子状态" width="95" align="center">
        <template #default="scope">
          <span v-if="scope.row.status === 0">草稿</span>
          <span v-if="scope.row.status === 3">待审核</span>
          <span v-if="scope.row.status === 1">已发布</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="postTime"
        label="发帖时间"
        width="160"
        align="center"
      />
      <el-table-column
        prop="likeCount"
        label="点赞数"
        width="80"
        align="center"
      />
      <el-table-column
        prop="commentCount"
        label="评论数"
        width="80"
        align="center"
      />
      <el-table-column
        prop="collectCount"
        label="收藏数"
        width="80"
        align="center"
      />
      <el-table-column align="center" width="100" label="操作">
        <template #default="scope">
          <el-button
            :icon="Delete"
            type="danger"
            size="default"
            @click="handleDelete(scope.row)"
            v-if="
              hasAuth('ltt:post:delete') &&
              (scope.row.status === 1 || scope.row.status === 3)
            "
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
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    />

    <!-- 图片预览对话框 -->
    <el-dialog
      v-model="previewDialogVisible"
      width="80%"
      :before-close="handlePreviewClose"
      center
    >
      <el-carousel :interval="5000" height="400px" indicator-position="none">
        <el-carousel-item v-for="(image, index) in images" :key="index">
          <img
            :src="image.url"
            style="width: 100%; height: 100%; object-fit: contain"
          />
        </el-carousel-item>
      </el-carousel>
    </el-dialog>
  </el-main>
</template>

<script setup lang="ts">
import postApi from "@/api/post";
import DictSelect from "@/components/system/DictSelect.vue";
import { Delete, Search, Check, SoldOut } from "@element-plus/icons-vue";
import { hasAuth } from "@/plugins/permission";
import { confirm as myConfirm, resetForm } from "@/utils";
import type { FormInstance } from "element-plus";

const postSearchModel = reactive({
  nickName: "",
  title: "",
  content: "",
  betweenTime: "",
  status: "",
});
const postList = ref<any[]>([]);
const tableHeight = ref(0);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);
const images = ref([]);
const previewDialogVisible = ref(false);

const searchForm = ref<FormInstance>();
const postTable = ref<FormInstance>();

const search = async (pageNo = 1, pageSize = 10) => {
  const params = {
    pageNo: pageNo,
    pageSize: pageSize,
    ...postSearchModel,
  };
  const { records, total: totalVal } = await postApi.getPostList(params);
  postList.value = records;
  total.value = totalVal;
};

watchEffect(() => {
  tableHeight.value = window.innerHeight - 220;
});

onMounted(() => {
  search();
});

const resetValue = () => {
  resetForm(searchForm.value, postSearchModel);
  search();
};

const handlePreview = (row: any) => {
  if (row.images.length > 0) {
    images.value = row.images;
    previewDialogVisible.value = true;
  } else {
    ElMessage.warning("没有帖子图片可以预览");
  }
};

const handlePreviewClose = () => {
  previewDialogVisible.value = false;
};

const handleDelete = async (row: any) => {
  const confirm = await myConfirm(`确定要删除帖子【${row.title}】吗?`);

  if (confirm) {
    const { message } = await postApi.deletePostById(row.id);
    ElMessage.success(message);
    search(pageNo.value, pageSize.value);
  }
};

const handelSelectAll = (val: any) => {
  if (val.length > 0) {
    postList.value.forEach((item) => {
      item.check = true;
    });
  } else {
    postList.value.forEach((item) => {
      item.check = false;
    });
  }
};

const handelItemCheckChange = (row: any) => {
  const allChecked = postList.value.every((post) => post.check);
  if (allChecked) {
    postTable.value?.toggleAllSelection();
  } else {
    postTable.value?.clearSelection();
  }
};

const batchOpertor = async (status: number) => {
  const selectedData = postList.value.filter((item) => item.check === true);
  if (selectedData.length <= 0) {
    ElMessage.warning("没有要操作的帖子");
    return;
  }
  if (selectedData.some((s) => s.status === 0)) {
    ElMessage.warning("草稿不需要审核");
    return;
  }
  const confirm = await myConfirm("确定要执行操作吗?");

  if (confirm) {
    selectedData.forEach((item) => {
      item.status = status;
    });
    const { message } = await postApi.batchUpdatePost(selectedData);
    ElMessage.success(message);
    search(pageNo.value, pageSize.value);
  }
};

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  search(pageNo.value, size);
};

const handleCurrentChange = (page: number) => {
  pageNo.value = page;
  search(page, pageSize.value);
};
</script>

<style scoped></style>
