<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form
      :model="categorySearchModel"
      ref="searchForm"
      label-width="80px"
      :inline="true"
      size="small"
    >
      <el-form-item>
        <el-input v-model="categorySearchModel.name" placeholder="分类名称" />
      </el-form-item>
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
          :icon="Plus"
          size="small"
          type="success"
          @click="openAddWindow()"
          v-if="hasAuth('ltt:category:add')"
        >
          新增
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 商品分类表格数据 -->
    <el-table
      :height="tableHeight"
      :data="categoryList"
      border
      stripe
      style="width: 100%; margin-bottom: 10px"
    >
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="parentName" label="父级分类" />
      <el-table-column prop="imageUrl" label="分类图标" />
      <el-table-column align="center" width="250" label="操作">
        <template #default="scope">
          <el-button
            :icon="Edit"
            type="primary"
            size="default"
            @click="handleEdit(scope.row)"
            v-if="hasAuth('ltt:category:edit')"
          >
            编辑
          </el-button>
          <el-button
            :icon="Delete"
            type="danger"
            size="default"
            @click="handleDelete(scope.row)"
            v-if="hasAuth('ltt:category:delete')"
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
    <!-- 添加和编辑分类窗口 -->
    <system-dialog
      :title="categoryDialog.title"
      :height="categoryDialog.height"
      :width="categoryDialog.width"
      :visible="categoryDialog.visible"
      @on-close="onCategoryClose"
      @on-confirm="onCategoryConfirm"
    >
      <template #content>
        <div>
          <el-form
            :model="category"
            ref="categoryForm"
            :rules="categoryRules"
            label-width="80px"
            :inline="true"
            size="small"
          >
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="category.name" />
            </el-form-item>
            <el-form-item label="父级分类">
              <el-cascader
                ref="categoryCascader"
                :options="categoryTreeList"
                :props="{ checkStrictly: true }"
                @change="handleCategoryChange"
              />
            </el-form-item>
            <el-form-item label="分类图标" prop="imageUrl">
              <el-upload
                action="/api/oss/file/multiUpload?module=letao-images"
                :headers="uploadHeaders"
                :show-file-list="false"
                :on-success="handleCategoryImageSuccess"
                :before-upload="beforeCategoryImageUpload"
              >
                <img
                  v-if="category.imageUrl"
                  :src="category.imageUrl"
                  class="avatar"
                />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-form>
        </div>
      </template>
    </system-dialog>
  </el-main>
</template>

<script setup lang="ts">
import categoryApi from "@/api/product-category";
import { TOKEN_KEY } from "@/enums/CacheEnum";
import type { FormInstance } from "element-plus";
import { Delete, Plus, Search, Edit } from "@element-plus/icons-vue";
import { hasAuth } from "@/plugins/permission";
import { objCopy, confirm as myConfirm, resetForm } from "@/utils";
const token = localStorage.getItem(TOKEN_KEY);

const categorySearchModel = reactive({ name: "" });
const categoryList = ref<any[]>([]);
const categoryTreeList = ref<any[]>([]);
const tableHeight = ref(0);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);
const categoryDialog = reactive({
  title: "",
  height: 400,
  width: 600,
  visible: false,
});
const category = reactive({ id: "", name: "", parentId: 0, imageUrl: "" });
const categoryRules = reactive({
  name: [{ required: true, message: "请填写分类名称", trigger: "blur" }],
});
const uploadHeaders = reactive({ token });

const searchForm = ref<FormInstance>();
const categoryForm = ref<FormInstance>();
const categoryCascader = ref<FormInstance>();

const search = async (pageNo = 1, pageSize = 10) => {
  const params = {
    pageNo: pageNo,
    pageSize: pageSize,
    ...categorySearchModel,
  };
  const { records, total: totalVal } =
    await categoryApi.getCategoryList(params);
  categoryList.value = records.map((item: any) => ({
    ...item,
    parentName: findParentName(item),
  }));
  total.value = totalVal;
};

const findParentName = (item: any) => {
  const parent = categoryList.value.find(
    (parent) => parent.id === item.parentId
  );

  return parent?.name || "";
};

watchEffect(() => {
  tableHeight.value = window.innerHeight - 220;
});

onMounted(() => {
  search();
  getCategoryTreeList();
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
  resetForm(searchForm.value, categorySearchModel);
  search();
};

const openAddWindow = () => {
  resetForm(categoryForm.value, category);
  categoryDialog.visible = true;
  categoryDialog.title = "添加商品分类";
};

const onCategoryClose = () => {
  categoryDialog.visible = false;
};

const handleEdit = (row: any) => {
  categoryDialog.title = "编辑商品分类";
  categoryDialog.visible = true;
  objCopy(row, category);
};

const onCategoryConfirm = async () => {
  const valid = await categoryForm.value?.validate();
  if (valid) {
    const { message } = await (category.id === ""
      ? categoryApi.addCategory(category)
      : categoryApi.updateCategory(category));
    ElMessage.success(message);
    search(pageNo.value, pageSize.value);
    getCategoryTreeList();
    categoryDialog.visible = false;
  }
};

const handleDelete = async (row: any) => {
  const confirm = await myConfirm("确定要删除该数据吗?");
  if (confirm) {
    const { message } = await categoryApi.deleteCategoryById(row.id);
    ElMessage.success(message);
    search(pageNo.value, pageSize.value);
    getCategoryTreeList();
  }
};

const getCategoryTreeList = async () => {
  const data = await categoryApi.getCategoryTreeList();
  categoryTreeList.value = data;
};

const handleCategoryChange = (value: any) => {
  if (value && value.length > 0) {
    category.parentId = value[value.length - 1];
  }
};

const handleCategoryImageSuccess = (data: any, file: any) => {
  category.imageUrl = data.url;
};

const beforeCategoryImageUpload = (file: any) => {
  const isJPG = file.type === "image/jpeg";
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isJPG) {
    ElMessage.error("上传分类图标只能是 JPG 格式!");
  }
  if (!isLt2M) {
    ElMessage.error("上传分类图标大小不能超过 2MB!");
  }
  return isJPG && isLt2M;
};
</script>

<style lang="scss">
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
</style>
