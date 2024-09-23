<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form
      :model="productSearchModel"
      ref="searchForm"
      label-width="80px"
      :inline="false"
      size="small"
    >
      <el-row>
        <el-col :span="6">
          <el-form-item label="是否推荐">
            <el-switch
              v-model="productSearchModel.isRecommended"
              active-value="1"
              inactive-value="0"
              active-text="是"
              inactive-text="否"
            />
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="商品状态">
            <dict-select
              dictCode="productStatus"
              v-model="productSearchModel.status"
              placeholder="商品状态"
            />
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="商品分类">
            <el-cascader
              :options="categoryList"
              :show-all-levels="false"
              v-model="selectedOptions"
              @change="onCategoryChange"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="发布者">
            <el-input
              v-model="productSearchModel.nickName"
              placeholder="发布者"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="商品名">
            <el-input v-model="productSearchModel.name" placeholder="商品名" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="商品描述">
            <el-input
              v-model="productSearchModel.description"
              placeholder="商品描述"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="发布时间" prop="publishTime">
            <el-date-picker
              v-model="productSearchModel.betweenTime"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD HH:mm:ss"
              unlink-panels
            />
          </el-form-item>
        </el-col>
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
              size="small"
              type="success"
              @click="batchOpertor(3, 1)"
              v-if="hasAuth('ltt:product:audit')"
            >
              批量审核
            </el-button>
            <el-button
              :icon="Pointer"
              size="small"
              type="success"
              @click="batchOpertor(0)"
              v-if="hasAuth('ltt:product:recommend')"
            >
              批量推荐
            </el-button>
            <el-button
              :icon="SoldOut"
              size="small"
              type="danger"
              @click="batchOpertor(1, 2)"
              v-if="hasAuth('ltt:product:soldOut')"
            >
              批量下架
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <!-- 商品表格数据 -->
    <el-table
      ref="productTable"
      :height="tableHeight"
      :data="productList"
      border
      stripe
      @select-all="handelSelectAll"
      style="width: 100%; margin-bottom: 10px"
    >
      <el-table-column type="selection" prop="check" width="55" align="center">
        <template #default="{ row }">
          <el-checkbox
            v-model="row.check"
            :checked="row.check"
            @change="handelItemCheckChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="id" label="商品编号" align="center" width="85" />
      <el-table-column prop="nickName" label="发布者" align="center" />
      <el-table-column prop="name" label="商品名" align="center">
        <template #default="{ row }">
          <el-tooltip effect="dark" :content="row.name" placement="top-start">
            <span>{{ row.name }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="商品描述" align="center">
        <template #default="{ row }">
          <el-tooltip
            effect="dark"
            :content="row.description"
            placement="top-start"
          >
            <span>{{ row.description }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="images" label="商品图片" width="80">
        <template #default="{ row }">
          <el-button size="default" @click="handlePreview(row)">预览</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" align="center" width="75" />
      <el-table-column
        prop="publishTime"
        label="发布时间"
        width="160"
        align="center"
      />
      <el-table-column prop="status" label="商品状态" align="center" width="80">
        <template #default="{ row }">
          <span v-if="row.status === 0">草稿</span>
          <span v-if="row.status === 1">上架</span>
          <span v-if="row.status === 2">下架</span>
          <span v-if="row.status === 3">待审核</span>
          <span v-if="row.status === 4">已售出</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="isRecommended"
        label="是否推荐"
        width="80"
        align="center"
      >
        <template #default="{ row }">
          <span v-if="row.isRecommended === 0">否</span>
          <span v-if="row.isRecommended === 1">是</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="categoryName"
        label="商品分类"
        width="100"
        align="center"
      />
      <el-table-column align="center" width="100" label="操作">
        <template #default="{ row }">
          <el-button
            :icon="Delete"
            type="danger"
            size="default"
            @click="handleDelete(row)"
            v-if="
              hasAuth('ltt:product:delete') &&
              (row.status === 1 || row.status === 3)
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
import productApi from "@/api/product";
import categoryApi from "@/api/product-category";
import DictSelect from "@/components/system/DictSelect.vue";
import {
  Delete,
  Search,
  Check,
  SoldOut,
  Pointer,
} from "@element-plus/icons-vue";
import { hasAuth } from "@/plugins/permission";
import { confirm as myConfirm, resetForm } from "@/utils";
import type { FormInstance } from "element-plus";

const productSearchModel = reactive({
  nickName: "",
  name: "",
  description: "",
  betweenTime: "",
  status: "",
  isRecommended: "",
  categoryId: 0,
});
const productList = ref<any[]>([]);
const categoryList = ref<any[]>([]);
const selectedOptions = ref([]);
const tableHeight = ref(0);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);
const images = ref([]);
const previewDialogVisible = ref(false);

const searchForm = ref<FormInstance>();
const productTable = ref<FormInstance>();

const search = async (pageNo = 1, pageSize = 10) => {
  const params = {
    pageNo: pageNo,
    pageSize: pageSize,
    ...productSearchModel,
  };

  const { records, total: totalVal } = await productApi.getProductList(params);
  productList.value = records;
  total.value = totalVal;

  const categories = await categoryApi.getCategoryTreeList();
  categoryList.value = categories;
};

watchEffect(() => {
  tableHeight.value = window.innerHeight - 220;
});

onMounted(() => {
  search();
});

const resetValue = () => {
  resetForm(searchForm.value, productSearchModel);
  selectedOptions.value = [];
  search();
};

const handlePreview = (row: any) => {
  if (row.images.length > 0) {
    images.value = row.images;
    previewDialogVisible.value = true;
  } else {
    ElMessage.warning("没有商品图片可以预览");
  }
};

const handlePreviewClose = () => {
  previewDialogVisible.value = false;
};

const handleDelete = async (row: any) => {
  const confirm = await myConfirm(
    `确定要删除商品名称为【${row.name}】的商品吗?`
  );
  if (confirm) {
    const { message } = await productApi.deleteProductById(row.id);
    ElMessage.success(message);
    search(pageNo.value, pageSize.value);
  }
};

const onCategoryChange = (value: any) => {
  productSearchModel.categoryId =
    value.length > 0 ? value[value.length - 1] : 0;
};

const handelSelectAll = (val: any) => {
  if (val.length > 0) {
    productList.value.forEach((item) => {
      item.check = true;
    });
  } else {
    productList.value.forEach((item) => {
      item.check = false;
    });
  }
};

const handelItemCheckChange = (row: any) => {
  const allChecked = productList.value.every((product) => product.check);
  if (allChecked) {
    productTable.value?.toggleAllSelection();
  } else {
    productTable.value?.clearSelection();
  }
};

const batchOpertor = async (status1: number, status2: number) => {
  const selectedData = productList.value.filter((item) => item.check);
  if (selectedData.length <= 0) {
    ElMessage.warning("没有要操作的商品");
    return;
  }
  if (selectedData.some((s) => s.status === 0)) {
    ElMessage.warning("草稿不需要审核");
    return;
  }
  if (selectedData.some((s) => s.status === 4)) {
    ElMessage.warning("已售出的商品不能进行操作");
    return;
  }

  const confirm = await myConfirm("确定要执行操作吗?");

  if (confirm) {
    if (status1 !== 0) {
      selectedData.forEach((item) => {
        item.status = status2;
      });
    } else {
      selectedData.forEach((item) => {
        item.isRecommended = item.isRecommended === 1 ? 0 : 1;
      });
    }

    const { message } = await productApi.batchUpdateProduct(selectedData);
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

<style lang="scss" scoped></style>
