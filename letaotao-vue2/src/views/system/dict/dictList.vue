<template>
  <div class="app-container">
    <div class="el-toolbar">
      <div class="el-toolbar-body" style="justify-content: flex-start">
        <el-button type="text" @click="exportData"><i class="fa fa-plus" /> 导出</el-button>
        <el-button type="text" @click="importData"
          ><i class="fa fa-plus" /> 导入</el-button
        >
      </div>
    </div>

    <el-table
      :data="list"
      style="width: 100%"
      row-key="id"
      border
      default-expand-all
      :tree-props="{ children: 'children' }"
    >
      <el-table-column label="名称" width="230" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="编码" width="220">
        <template slot-scope="{ row }">
          {{ row.dictCode }}
        </template>
      </el-table-column>
      <el-table-column label="字符值" width="230" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row.strValue }}</span>
        </template>
      </el-table-column>

      <el-table-column label="数字值" width="230" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row.numValue }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="230" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="导入" :visible.sync="dialogImportVisible" width="480px">
      <el-form label-position="right" label-width="170px">
        <el-form-item label="文件">
          <el-upload
            :multiple="false"
            :on-success="onUploadSuccess"
            :action="baseUrl + '/importData'"
            :data="uploadHeader"
            class="upload-demo"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传excel文件，且不超过500kb
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogImportVisible = false"> 取消 </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import dict from "@/api/dict";
//导入token
import { getToken } from "@/utils/auth";
export default {
  data() {
    return {
      baseUrl: `${process.env.VUE_APP_BASE_API}/api/dictionaries`,
      //上传需要携带的数据
      uploadHeader: { token: getToken() },
      dialogImportVisible: false, //设置弹框是否弹出
      list: [], //数据字典列表数组
    };
  },
  created() {
    this.getDictList();
  },
  methods: {
    //导入数据字典
    importData() {
      this.dialogImportVisible = true;
    },
    //上传成功调用
    onUploadSuccess() {
      //关闭弹框
      this.dialogImportVisible = false;
      //刷新页面
      this.getDictList();
    },
    //导出数据字典
    async exportData() {
      //调用导出接口
      const exportData = await dict.exportDict();
    },
    //数据字典列表
    getDictList() {
      dict.getDictList().then((response) => {
        this.list = response.data;
      });
    },
  },
};
</script>
