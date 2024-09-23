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
            <el-input
              v-model="postSearchModel.nickName"
              placeholder="发布者"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="标题">
            <el-input
              v-model="postSearchModel.title"
              placeholder="标题"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="帖子内容">
            <el-input
              v-model="postSearchModel.content"
              placeholder="帖子内容"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="发帖时间" prop="betweenTime">
            <el-date-picker
              v-model="postSearchModel.betweenTime"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              unlink-panels
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="帖子状态">
            <dict-select
              dictCode="postStatus"
              v-model="postSearchModel.status"
              placeholder="帖子状态"
            ></dict-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
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
              @click="batchOpertor(1)"
              v-if="hasPermission('ltt:post:audit')"
            >
              批量通过
            </el-button>
            <el-button
              icon="el-icon-sold-out"
              size="small"
              type="danger"
              @click="batchOpertor(3)"
              v-if="hasPermission('ltt:post:out')"
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
        label="帖子编号"
        width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="nickName"
        label="发布者"
        width="100"
        align="center"
      ></el-table-column>
      <el-table-column prop="title" label="标题" width="120" align="center">
        <template slot-scope="scope">
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
        width="120"
        align="center"
      >
        <template slot-scope="scope">
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
        <template slot-scope="scope">
          <el-button size="mini" @click="handlePreview(scope.row)"
            >预览</el-button
          >
        </template>
      </el-table-column>
      <el-table-column prop="status" label="帖子状态" width="85" align="center">
        <template slot-scope="scope">
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
      ></el-table-column>
      <el-table-column
        prop="likeCount"
        label="点赞数"
        width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="commentCount"
        label="评论数"
        width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="collectCount"
        label="收藏数"
        width="80"
        align="center"
      ></el-table-column>
      <el-table-column align="center" width="100" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-delete"
            type="danger"
            size="mini"
            @click="handleDelete(scope.row)"
            v-if="hasPermission('ltt:post:delete') && (scope.row.status === 1 || scope.row.status === 3)"
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
import postApi from "@/api/post";
import DictSelect from '@/components/system/DictSelect';
export default {
  name: "postList",
  components: { DictSelect },
  data() {
    return {
      // 查询条件对象
      postSearchModel: {
        nickName: "",
        title: "",
        content: "",
        betweenTime: "",
        status: "",
      },
      postList: [], // 帖子列表
      tableHeight: 0, // 表格高度
      pageNo: 1, // 当前页码
      pageSize: 10, // 每页显示数量
      total: 0, // 总数量
      images: [], //图片列表
      previewDialogVisible: false, // 预览图片对话框是否可见
    };
  },
  mounted() {
    // 获取帖子列表
    this.search();
    // 表格高度
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  methods: {
    /** * 查询帖子列表 */
    async search(pageNo = 1, pageSize = 10) {
      let params = {
        pageNo: pageNo,
        pageSize: pageSize,
        ...this.postSearchModel,
      };
      // 发送查询请求
      let res = await postApi.getPostList(params);
      if (res.success) {
        this.postList = res.data.records;
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
      this.$resetForm("searchForm", this.postSearchModel);
      // 重新查询
      this.search();
    },
    handlePreview(row) {
      if (row.images.length > 0) {
        this.images = row.images;
        this.previewDialogVisible = true;
      } else {
        this.$message.warning("没有帖子图片可以预览");
      }
    },
    handlePreviewClose() {
      this.previewDialogVisible = false;
    },
    /** * 删除 */
    async handleDelete(row) {
      let confirm = await this.$myconfirm(`确定要删除帖子【${row.name}】吗?`);
      if (confirm) {
        // 封装条件
        let params = { id: row.id };
        // 发送删除请求
        let res = await postApi.deletePostById(params);
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
    handelSelectAll(val) {
      if(val.length > 0) {
        this.postList.forEach((post) => {
          post.check = true;
        });
      } else {
        this.postList.forEach((post) => {
          post.check = false;
        });
      }
    },
    handelItemCheckChange() {
      let allChecked = this.postList.every((post) => post.check);
      // 如果所有帖子都被选中，则将头部的checkbox设置为true
      if (allChecked) {
        this.$refs.postTable.toggleAllSelection();
      } else {
        this.$refs.postTable.clearSelection();
      }
    },
    /** * 批量操作 */
    async batchOpertor(status) {
      // 获取已选中的数据
      let selectedData = this.postList.filter((item) => item.check === true);
      if (selectedData.length <= 0) {
        this.$message.warning("没有要操作的帖子");
        return;
      }
      if (selectedData.filter((s) => s.status === 0).length > 0) {
        this.$message.warning("草稿不需要审核");
        return;
      }
      let confirm = await this.$myconfirm("确定要执行操作吗?");
      if (confirm) {
        //设置帖子状态
        selectedData.forEach((item) => {
          item.status = status;
        });
        //更新帖子
        let res = await postApi.batchUpdatePost(selectedData);
        if (res.success) {
          this.$message.success(res.message);
          this.search();
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped></style>
