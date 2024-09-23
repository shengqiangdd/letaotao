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
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="商品状态">
            <dict-select
              dictCode="productStatus"
              v-model="productSearchModel.status"
              placeholder="商品状态"
            ></dict-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="商品分类">
            <el-cascader
              :options="categoryList"
              :show-all-levels="false"
              v-model="selectedOptions"
              @change="onCategoryChange"
            ></el-cascader>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="发布者">
            <el-input
              v-model="productSearchModel.nickName"
              placeholder="发布者"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="商品名">
            <el-input
              v-model="productSearchModel.name"
              placeholder="商品名"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="商品描述">
            <el-input
              v-model="productSearchModel.description"
              placeholder="商品描述"
            ></el-input>
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
              value-format="yyyy-MM-dd HH:mm:ss"
              unlink-panels
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="18">
          <el-form-item>
            <el-button
              icon="el-icon-search"
              type="primary"
              @click="search(pageNo, pageSize)"
            >
              查询
            </el-button>
            <el-button icon="el-icon-delete" @click="resetValue()"
              >重置</el-button
            >
            <el-button
              icon="el-icon-check"
              size="small"
              type="success"
              @click="batchOpertor(3, 1)"
              v-if="hasPermission('ltt:product:audit')"
            >
              批量审核
            </el-button>
            <el-button
              icon="el-icon-thumb"
              size="small"
              type="success"
              @click="batchOpertor(0)"
              v-if="hasPermission('ltt:product:recommend')"
            >
              批量推荐
            </el-button>
            <el-button
              icon="el-icon-sold-out"
              size="small"
              type="danger"
              @click="batchOpertor(1, 2)"
              v-if="hasPermission('ltt:product:soldOut')"
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
        <template slot-scope="scope">
          <el-checkbox
            v-model="scope.row.check"
            :checked="scope.row.check"
            @change="handelItemCheckChange()"
          ></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column
        prop="id"
        label="商品编号"
        align="center"
        width="85"
      ></el-table-column>
      <el-table-column
        prop="nickName"
        label="发布者"
        align="center"
      ></el-table-column>
      <el-table-column prop="name" label="商品名" align="center">
        <template slot-scope="scope">
          <el-tooltip
            effect="dark"
            :content="scope.row.name"
            placement="top-start"
          >
            <span>{{ scope.row.name }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="商品描述" align="center">
        <template slot-scope="scope">
          <el-tooltip
            effect="dark"
            :content="scope.row.description"
            placement="top-start"
          >
            <span>{{ scope.row.description }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="images" label="商品图片" width="80">
        <template slot-scope="scope">
          <el-button size="mini" @click="handlePreview(scope.row)"
            >预览</el-button
          >
        </template>
      </el-table-column>
      <el-table-column
        prop="price"
        label="价格"
        align="center"
        width="75"
      ></el-table-column>
      <el-table-column
        prop="publishTime"
        label="发布时间"
        width="160"
        align="center"
      ></el-table-column>
      <el-table-column prop="status" label="商品状态" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0">草稿</span>
          <span v-if="scope.row.status === 1">上架</span>
          <span v-if="scope.row.status === 2">下架</span>
          <span v-if="scope.row.status === 3">待审核</span>
          <span v-if="scope.row.status === 4">已售出</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="isRecommended"
        label="是否推荐"
        width="80"
        align="center"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.isRecommended == 0">否</span>
          <span v-if="scope.row.isRecommended == 1">是</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="categoryName"
        label="商品分类"
        width="100"
        align="center"
      ></el-table-column>
      <el-table-column align="center" width="100" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-delete"
            type="danger"
            size="mini"
            @click="handleDelete(scope.row)"
            v-if="
              hasPermission('ltt:product:delete') &&
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
      :page-size="10"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

    <!-- 图片预览对话框 -->
    <el-dialog
      :visible.sync="previewDialogVisible"
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
<script>
import productApi from "@/api/product";
import categoryApi from "@/api/product-category";
import DictSelect from "@/components/system/DictSelect";
export default {
  name: "productList",
  components: { DictSelect },
  data() {
    return {
      // 查询条件对象
      productSearchModel: {
        nickName: "",
        name: "",
        description: "",
        betweenTime: "",
        status: "",
        isRecommended: "",
        categoryId: 0,
      },
      productList: [], // 商品列表
      categoryList: [],
      selectedOptions: [],
      tableHeight: 0, // 表格高度
      pageNo: 1, // 当前页码
      pageSize: 10, // 每页显示数量
      total: 0, // 总数量
      images: [], //图片列表
      previewDialogVisible: false, // 预览图片对话框是否可见
    };
  },
  mounted() {
    // 获取商品列表
    this.search();
    // 表格高度
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  methods: {
    /** * 查询商品列表 */
    async search(pageNo = 1, pageSize = 10) {
      let params = {
        pageNo: pageNo,
        pageSize: pageSize,
        ...this.productSearchModel,
      };
      // 发送查询请求
      let res = await productApi.getProductList(params);
      if (res.success) {
        this.productList = res.data.records;
        this.total = res.data.total;
        res = await categoryApi.getCategoryTreeList();
        if (res.success) {
          this.categoryList = res.data;
        }
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
      this.$resetForm("searchForm", this.productSearchModel);
      this.selectedOptions = [];
      // 重新查询
      this.search();
    },
    handlePreview(row) {
      if (row.images.length > 0) {
        this.images = row.images;
        this.previewDialogVisible = true;
      } else {
        this.$message.warning("没有商品图片可以预览");
      }
    },
    handlePreviewClose() {
      this.previewDialogVisible = false;
    },
    /** * 删除 */
    async handleDelete(row) {
      let confirm = await this.$myconfirm(
        `确定要删除商品名称为【${row.name}】的商品吗?`
      );
      if (confirm) {
        // 封装条件
        let params = { id: row.id };
        // 发送删除请求
        let res = await productApi.deleteProductById(params);
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
    onCategoryChange(value) {
      this.productSearchModel.categoryId =
        value.length > 0 ? value[value.length - 1] : 0;
    },
    handelSelectAll(val) {
      if (val.length > 0) {
        this.productList.forEach((product) => {
          product.check = true;
        });
      } else {
        this.productList.forEach((product) => {
          product.check = false;
        });
      }
    },
    handelItemCheckChange() {
      let allChecked = this.productList.every((product) => product.check);
      // 如果所有商品都被选中，则将头部的checkbox设置为true
      if (allChecked) {
        this.$refs.productTable.toggleAllSelection();
      } else {
        this.$refs.productTable.clearSelection();
      }
    },
    /** * 批量操作 */
    async batchOpertor(status1, status2) {
      // 获取已选中的数据
      let selectedData = this.productList.filter((item) => item.check);
      console.log(selectedData);
      if (selectedData.length <= 0) {
        this.$message.warning("没有要操作的商品");
        return;
      }
      if (selectedData.filter((s) => s.status === 0).length > 0) {
        this.$message.warning("草稿不需要审核");
        return;
      }
      if (selectedData.filter((s) => s.status === 4).length > 0) {
        this.$message.warning("已售出的商品不能进行操作");
        return;
      }
      let confirm = await this.$myconfirm("确定要执行操作吗?");
      if (confirm) {
        if (status1 != 0) {
          //设置商品状态
          selectedData.forEach((item) => {
            item.status = status2;
          });
        } else {
          //设置推荐状态
          selectedData.forEach((item) => {
            item.isRecommended = item.isRecommended === 1 ? 0 : 1;
          });
        }
        //更新商品
        let res = await productApi.batchUpdateProduct(selectedData);
        if (res.success) {
          this.$message.success(res.message);
          this.search();
        } else {
          this.$message.error(res.message);
        }
      }
    },
  },
};
</script>
<style lang="scss" scoped></style>
