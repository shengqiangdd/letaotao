<template>
  <el-container :style="{ height: containerHeight + 'px' }">
    <!-- 表格数据 -->
    <el-main>
      <!-- 查询条件 -->
      <el-form
        :model="searchModel"
        ref="searchForm"
        label-width="80px"
        :inline="true"
        size="small"
      >
        <el-form-item>
          <el-input v-model="searchModel.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="searchModel.realName"
            placeholder="请输入真实姓名"
          />
        </el-form-item>
        <el-form-item>
          <el-input v-model="searchModel.phone" placeholder="请输入电话" />
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
            v-if="hasAuth('sys:user:add')"
          >
            新增
          </el-button>
        </el-form-item>
      </el-form>
      <!-- 用户表格数据 -->
      <el-table
        :height="tableHeight"
        :data="userList"
        border
        stripe
        style="width: 100%; margin-bottom: 10px"
      >
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="realName" label="姓名" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column align="center" width="290" label="操作">
          <template #default="scope">
            <el-button
              :icon="Edit"
              type="primary"
              size="small"
              @click="handleEdit(scope.row)"
              v-if="hasAuth('sys:user:edit')"
            >
              编辑
            </el-button>
            <el-button
              :icon="Delete"
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
              v-if="hasAuth('sys:user:delete')"
            >
              删除
            </el-button>
            <el-button
              :icon="Setting"
              type="primary"
              size="small"
              @click="assignRole(scope.row)"
              v-if="hasAuth('sys:user:assign') && !scope.row.openId"
            >
              分配角色
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
    <!-- 添加和编辑用户窗口 -->
    <system-dialog
      :title="userDialog.title"
      :height="userDialog.height"
      :width="userDialog.width"
      :visible="userDialog.visible"
      @on-close="onClose"
      @on-confirm="onConfirm"
    >
      <template #content>
        <el-form
          :model="user"
          ref="userForm"
          :rules="rules"
          label-width="80px"
          :inline="true"
          size="small"
        >
          <el-form-item prop="username" label="用户名">
            <el-input v-model="user.username" />
          </el-form-item>
          <el-form-item prop="password" v-if="user.id === 0" label="密码">
            <el-input type="password" v-model="user.password" />
          </el-form-item>
          <el-form-item prop="realName" label="姓名">
            <el-input v-model="user.realName" />
          </el-form-item>
          <el-form-item prop="phone" label="电话">
            <el-input v-model="user.phone" />
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="user.nickName" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="user.email" />
          </el-form-item>
          <el-form-item prop="gender" label="性别">
            <el-radio-group v-model="user.gender">
              <el-radio :value="0">男</el-radio>
              <el-radio :value="1">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <br />
          <!-- 用户头像 -->
          <el-form-item label="头像">
            <el-upload
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              class="avatar-uploader"
              :data="uploadHeader"
              :action="uploadUrl"
            >
              <img v-if="user.avatar" :src="user.avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-form>
      </template>
    </system-dialog>
    <!-- 分配角色窗口 -->
    <system-dialog
      :title="assignDialog.title"
      :height="assignDialog.height"
      :width="assignDialog.width"
      v-model:visible="assignDialog.visible"
      @on-close="onAssignClose"
      @on-confirm="onAssignConfirm"
    >
      <template #content>
        <!-- 分配角色数据列表 -->
        <el-table
          ref="assignRoleTable"
          :data="assignRoleList"
          border
          stripe
          :height="assignHeight"
          style="width: 100%; margin-bottom: 10px"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column prop="roleCode" label="角色编码" />
          <el-table-column prop="roleName" label="角色名称" />
          <el-table-column prop="remark" label="角色备注" />
        </el-table>
        <!-- 分页工具栏 -->
        <el-pagination
          @size-change="assignSizeChange"
          @current-change="assignCurrentChange"
          v-model:current-page="roleVo.pageNo"
          :page-sizes="[10, 20, 30, 40, 50]"
          :page-size="roleVo.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="roleVo.total"
          background
        />
      </template>
    </system-dialog>
  </el-container>
</template>

<script setup lang="ts">
import userApi from "@/api/user";
import SystemDialog from "@/components/system/SystemDialog.vue";
import { useUserStore } from "@/store";
import { objCopy, confirm as myConfirm, resetForm } from "@/utils";
import type { FormInstance } from "element-plus";
import { TOKEN_KEY } from "@/enums/CacheEnum";
import { Delete, Plus, Search, Edit, Setting } from "@element-plus/icons-vue";
import { hasAuth } from "@/plugins/permission";

const store = useUserStore(); // 使用 Pinia store

// 自定义验证规则
const validatePhone = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error("请输入手机号码"));
  } else if (!/^1[3456789]\d{9}$/.test(value)) {
    callback(new Error("手机号格式不正确"));
  } else {
    callback();
  }
};

const uploadUrl = ref(
  `${import.meta.env.VITE_APP_BASE_API}/api/oss/file/upload?module=avatar`
);

const userId = computed(() => store.user.id);

const containerHeight = ref(0);
const searchModel = reactive({
  username: "",
  realName: "",
  phone: "",
  pageNo: 1,
  pageSize: 10,
});
const userList = ref<any[]>([]);
const tableHeight = ref(0);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);
const userDialog = reactive({
  title: "",
  height: 410,
  width: 610,
  visible: false,
});
const user = reactive({
  id: 0,
  email: "",
  realName: "",
  phone: "",
  nickName: "",
  password: "",
  username: "",
  gender: "",
  avatar: "",
});
const token = localStorage.getItem(TOKEN_KEY);
const uploadHeader = reactive({ token });
const assignDialog = reactive({
  title: "",
  visible: false,
  width: 800,
  height: 410,
});
const roleVo = reactive({
  pageNo: 1,
  pageSize: 10,
  userId: "",
  total: 0,
});
const assignRoleList = ref<any[]>([]);
const assignHeight = ref(0);
const selectedIds = ref<string[]>([]);
const selectedUserId = ref("");

const searchForm = ref<FormInstance>();
const userForm = ref<FormInstance>();
const assignRoleTable = ref<any>();

const rules = reactive({
  realName: [{ required: true, trigger: "blur", message: "请填写姓名" }],
  phone: [{ required: true, trigger: "blur", validator: validatePhone }],
  username: [{ required: true, trigger: "blur", message: "请填写登录名" }],
  password: [{ required: true, trigger: "blur", message: "请填写登录密码" }],
  gender: [{ required: true, trigger: "change", message: "请选择性别" }],
});

const search = async (pageNo = 1, pageSize = 10) => {
  searchModel.pageNo = pageNo;
  searchModel.pageSize = pageSize;

  const data = await userApi.getUserList(searchModel);

  userList.value = data.records;
  total.value = data.total;
};

onMounted(() => {
  watchEffect(() => {
    containerHeight.value = window.innerHeight - 85;
    tableHeight.value = window.innerHeight - 220;
    assignHeight.value = window.innerHeight - 350;
  });
  search();
});

const handleSizeChange = (size: number) => {
  pageSize.value = size; //将每页显示的数量交给成员变量
  search(pageNo.value, size);
};

const handleCurrentChange = (page: number) => {
  pageNo.value = page;
  //调用查询方法
  search(page, pageSize.value);
};

const resetValue = () => {
  resetForm(searchForm.value, searchModel);
  search();
};

const openAddWindow = () => {
  resetForm(userForm.value, user);
  userDialog.title = "新增用户";
  userDialog.visible = true;
};

const onClose = () => {
  userDialog.visible = false;
};

const handleEdit = (row: any) => {
  objCopy(row, user);
  userDialog.title = "编辑用户";
  userDialog.visible = true;
};

const onConfirm = async () => {
  userForm.value?.validate(async (valid: boolean) => {
    if (valid) {
      let data = null;
      if (user.id === 0) {
        data = await userApi.addUser(user);
      } else {
        data = await userApi.updateUser(user);
      }
      ElMessage.success(data.message);
      search(pageNo.value, pageSize.value);
      userDialog.visible = false;
    }
  });
};

const handleDelete = async (row: any) => {
  const confirm = await myConfirm("确定要删除该数据吗?");

  if (confirm) {
    const data = await userApi.deleteUser(row.id);
    ElMessage.success(data.message);
    search(pageNo.value, pageSize.value);
  }
};

const handleAvatarSuccess = (res: any, file: any) => {
  user.avatar = res.data;
  // 强制重新渲染
  nextTick(() => {});
};

const beforeAvatarUpload = (file: any) => {
  const isImage =
    file.type === "image/jpeg" ||
    file.type === "image/png" ||
    file.type === "image/gif";
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isImage) {
    ElMessage.error("上传图像图片只能是 JPG、PNG、GIF 格式!");
  }
  if (!isLt2M) {
    ElMessage.error("上传图像图片文件大小不能超过 2MB");
  }
  return isImage && isLt2M;
};

const assignRole = async (row: any) => {
  selectedIds.value = [];
  selectedUserId.value = "";
  selectedUserId.value = row.id;
  assignDialog.visible = true;
  assignDialog.title = `给【${row.realName}】分配角色`;
  await getAssignRoleList();

  const data = await userApi.getRoleIdByUserId(row.id);
  selectedIds.value = data;
  selectedIds.value.forEach((key: string) => {
    assignRoleList.value.forEach((item: any) => {
      if (item.id === key) {
        assignRoleTable.value?.toggleRowSelection(item, true);
      }
    });
  });
};

const getAssignRoleList = async (pageNo = 1, pageSize = 10) => {
  roleVo.userId = userId.value;
  roleVo.pageNo = pageNo;
  roleVo.pageSize = pageSize;
  const data = await userApi.getAssignRoleList(roleVo);
  assignRoleList.value = data.records;
  roleVo.total = data.total;
};

const onAssignClose = () => {
  assignDialog.visible = false;
};

const onAssignConfirm = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning("请选择要分配的角色！");
    return;
  }
  const params = {
    userId: selectedUserId.value,
    roleIds: selectedIds.value,
  };
  const data = await userApi.assignRoleSave(params);
  ElMessage.success(data.message);
  assignDialog.visible = false;
};

const assignSizeChange = (size: number) => {
  roleVo.pageSize = size;
  getAssignRoleList(roleVo.pageNo, size);
};

const assignCurrentChange = (page: number) => {
  roleVo.pageNo = page;
  getAssignRoleList(page, roleVo.pageSize);
};

const handleSelectionChange = (rows: any[]) => {
  selectedIds.value = rows.map((item) => item.id);
};
</script>

<style lang="scss">
//用户头像
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9 !important;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar-uploader img {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
