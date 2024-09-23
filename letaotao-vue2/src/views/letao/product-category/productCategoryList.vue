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
        <el-input
          v-model="categorySearchModel.name"
          placeholder="分类名称"
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
        <el-button
          icon="el-icon-plus"
          size="small"
          type="success"
          @click="openAddWindow()"
          v-if="hasPermission('ltt:category:add')"
          >新增</el-button
        >
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
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="name" label="分类名称"></el-table-column>
      <el-table-column prop="parentName" label="父级分类"></el-table-column>
      <el-table-column prop="imageUrl" label="分类图标"></el-table-column>
      <el-table-column align="center" width="180" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-edit"
            type="primary"
            size="mini"
            @click="handleEdit(scope.row)"
            v-if="hasPermission('ltt:category:edit')"
            >编辑</el-button
          >
          <el-button
            icon="el-icon-delete"
            type="danger"
            size="mini"
            @click="handleDelete(scope.row)"
            v-if="hasPermission('ltt:category:delete')"
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
    <!-- 添加和编辑分类窗口 -->
    <system-dialog
      :title="categoryDialog.title"
      :height="categoryDialog.height"
      :width="categoryDialog.width"
      :visible="categoryDialog.visible"
      @onClose="onCategoryClose"
      @onConfirm="onCategoryConfirm"
    >
      <div slot="content">
        <el-form
          :model="category"
          ref="categoryForm"
          :rules="categoryRules"
          label-width="80px"
          :inline="true"
          size="small"
        >
          <el-form-item label="分类名称" prop="name">
            <el-input v-model="category.name"></el-input>
          </el-form-item>
          <el-form-item label="父级分类">
            <el-cascader
              ref="categoryCascader"
              :options="categoryTreeList"
              :props="{ checkStrictly: true }"
              @change="handleCategoryChange"
            ></el-cascader>
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
    </system-dialog>
  </el-main>
</template>
<script>
import categoryApi from "@/api/product-category";
import SystemDialog from "@/components/system/SystemDialog.vue";
import { getToken } from "@/utils/auth";
export default {
  name: "categoryList",
  components: { SystemDialog },
  data() {
    return {
      // 查询条件对象
      categorySearchModel: { name: "" },
      categoryList: [], // 商品分类列表
      categoryTreeList: [], // 商品分树列表
      tableHeight: 0, // 表格高度
      pageNo: 1, // 当前页码
      pageSize: 10, // 每页显示数量
      total: 0, // 总数量
      // 添加和修改商品分类窗口属性
      categoryDialog: { title: "", height: 400, width: 600, visible: false },
      // 商品分类对象
      category: { id: "", name: "", parentId: 0, imageUrl: "" },
      categoryRules: {
        name: [{ required: true, message: "请填写分类名称", trigger: "blur" }],
      },
      // 上传头部
      uploadHeaders: { token: getToken() },
      // 商品分类选择器属性
      categoryProps: { value: "id", label: "name", children: "children" },
    };
  },
  mounted() {
    // 获取商品分类列表
    this.search();
    this.getCategoryTreeList(); //获取商品分类树列表
    // 表格高度
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  methods: {
    /** * 查询商品分类列表 */
    async search(pageNo = 1, pageSize = 10) {
      let params = {
        pageNo: pageNo,
        pageSize: pageSize,
        ...this.categorySearchModel,
      };
      // 发送查询请求
      let res = await categoryApi.getCategoryPageList(params);
      if (res.success) {

        this.categoryList = res.data.records.map((item) => {
          item.parentName =
            (res.data.records || []).find(
              (parent) => parent.id == item.parentId
            )?.name || "";
          return item;
        });
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
      this.$resetForm("searchForm", this.categorySearchModel);
      // 重新查询
      this.search();
    },
    /** * 打开添加窗口 */
    openAddWindow() {
      this.$resetForm("categoryForm", this.category); // 清空表单
      this.categoryDialog.visible = true; // 显示窗口
      this.$nextTick(() => {
        // 清空 cascader 的选择状态
        this.$refs.categoryCascader.$children[1].clearCheckedNodes();
      });
      this.categoryDialog.title = "添加商品分类"; // 设置标题
    },
    /** * 新增或编辑取消事件 */
    onCategoryClose() {
      this.categoryDialog.visible = false; // 关闭窗口
    },
    /** * 编辑商品分类 */
    handleEdit(row) {
      // 设置弹框属性
      this.categoryDialog.title = "编辑商品分类";
      this.categoryDialog.visible = true;
      // 把当前编辑的数据复制到表单数据域，供回显使用
      this.$objCopy(row, this.category);
    },
    /** * 新增或编辑确认事件 */
    async onCategoryConfirm() {
      // 表单验证
      this.$refs.categoryForm.validate(async (valid) => {
        if (valid) {
          let res = null;
          // 判断商品分类编号是否为空
          if (this.category.id == "") {
            // 发送添加请求
            res = await categoryApi.addCategory(this.category);
          } else {
            // 发送修改请求
            res = await categoryApi.updateCategory(this.category);
          } // 判断是否成功
          if (res.success) {
            this.$message.success(res.message);
            this.search(this.pageNo, this.pageSize); // 刷新
            this.getCategoryTreeList(); //获取商品分类树列表
            this.categoryDialog.visible = false; // 关闭窗口
          } else {
            this.$message.error(res.message);
          }
        }
      });
    },
    /** * 删除商品分类 */
    async handleDelete(row) {
      let confirm = await this.$myconfirm("确定要删除该数据吗?");
      if (confirm) {
        // 封装条件
        let params = { id: row.id };
        // 发送删除请求
        let res = await categoryApi.deleteCategoryById(params);
        // 判断是否成功
        if (res.success) {
          this.$message.success(res.message);
          // 刷新
          this.search(this.pageNo, this.pageSize);
          this.getCategoryTreeList(); //获取商品分类树列表
        } else {
          this.$message.error(res.message);
        }
      }
    },
    /** * 获取商品分类树列表 */
    async getCategoryTreeList() {
      let res = await categoryApi.getCategoryTreeList();
      if (res.success) {
        this.categoryTreeList = res.data;
      }
    },
    /** * 商品分类选择框选择时触发事件 */
    handleCategoryChange(value) {
      if (value && value.length > 0) {
        console.log(value[value.length - 1]);
        this.category.parentId = value[value.length - 1];
      }
    },
    /** * 商品分类图标上传成功时触发事件 */
    handleCategoryImageSuccess(res, file) {
      this.category.imageUrl = res.data.url;
    },
    /** * 商品分类图标上传前校验 */
    beforeCategoryImageUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error("上传分类图标只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传分类图标大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
  },
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
