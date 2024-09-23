<template>
  <div class="app-container">
    <div class="el-toolbar">
      <div class="el-toolbar-body" style="justify-content: flex-start">
        <el-button type="default" @click="exportData">
          <i class="fa fa-plus"></i>
          导出
        </el-button>
        <el-button type="default" @click="importData">
          <i class="fa fa-plus"></i>
          导入
        </el-button>
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
        <template #default="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="编码" width="220">
        <template #row>
          {{ row.dictCode }}
        </template>
      </el-table-column>
      <el-table-column label="字符值" width="230" align="left">
        <template #default="scope">
          <span>{{ scope.row.strValue }}</span>
        </template>
      </el-table-column>

      <el-table-column label="数字值" width="230" align="left">
        <template #default="scope">
          <span>{{ scope.row.numValue }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="230" align="center">
        <template #default="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="导入" v-model="dialogImportVisible" width="480px">
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
            <template #tip>
              <div class="el-upload__tip">只能上传excel文件，且不超过500kb</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogImportVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import dict from "@/api/dict";
//token
import { TOKEN_KEY } from "@/enums/CacheEnum";
const token = localStorage.getItem(TOKEN_KEY);

const baseUrl = ref(`${import.meta.env.VITE_APP_BASE_API}/api/dictionaries`);
const uploadHeader = ref({ token }); //上传需要携带的数据
const dialogImportVisible = ref(false); //设置弹框是否弹出
const list = ref([]); //数据字典列表数组

//导入数据字典
const importData = () => {
  dialogImportVisible.value = true;
};

//上传成功调用
const onUploadSuccess = () => {
  //关闭弹框
  dialogImportVisible.value = false;
  //刷新页面
  getDictList();
};

//导出数据字典
const exportData = async () => {
  //调用导出接口
  await dict.exportDict();
};
//数据字典列表
const getDictList = () => {
  dict.getDictList().then((data) => {
    list.value = data;
  });
};

onMounted(() => {
  getDictList();
});
</script>

<script>
export default {
  name: "DictList",
};
</script>
