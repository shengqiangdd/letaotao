<template>
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
        <el-input v-model="searchModel.roleName" placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          :icon="Search"
          @click="search(pageNo, pageSize)"
        >
          查询
        </el-button>
        <el-button :icon="RefreshRight" @click="resetValue">重置</el-button>
        <el-button
          type="success"
          :icon="Plus"
          @click="openAddWindow"
          v-if="hasAuth('sys:role:add')"
        >
          新增
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 数据表格 -->
    <el-table
      :data="roleList"
      :height="tableHeight"
      border
      stripe
      style="width: 100%; margin-bottom: 10px"
    >
      <el-table-column prop="id" label="角色编号" width="100" align="center" />
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="remark" label="角色备注" />
      <el-table-column label="操作" align="center" width="290">
        <template #default="scope">
          <el-button
            :icon="Edit"
            type="primary"
            size="small"
            @click="handleEdit(scope.row)"
            v-if="hasAuth('sys:role:edit')"
          >
            编辑
          </el-button>
          <el-button
            :icon="Delete"
            type="danger"
            size="small"
            @click="handleDelete(scope.row)"
            v-if="hasAuth('sys:role:delete')"
          >
            删除
          </el-button>
          <el-button
            :icon="Setting"
            type="primary"
            size="small"
            @click="assignRole(scope.row)"
            v-if="hasAuth('sys:role:assign')"
          >
            分配权限
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

    <!-- 添加和修改角色窗口 -->
    <system-dialog
      :title="roleDialog.title"
      v-model:visible="roleDialog.visible"
      :width="roleDialog.width"
      :height="roleDialog.height"
      @on-close="onClose"
      @on-confirm="onConfirm"
    >
      <template #content>
        <el-form
          :model="role"
          ref="roleForm"
          :rules="rules"
          label-width="80px"
          :inline="false"
          size="small"
        >
          <el-form-item label="角色编码" prop="roleCode">
            <el-input v-model="role.roleCode" />
          </el-form-item>
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="role.roleName" />
          </el-form-item>
          <el-form-item label="角色描述">
            <el-input type="textarea" v-model="role.remark" :rows="5" />
          </el-form-item>
        </el-form>
      </template>
    </system-dialog>
    <!-- 分配权限树窗口 -->
    <system-dialog
      :title="assignDialog.title"
      v-model:visible="assignDialog.visible"
      :width="assignDialog.width"
      :height="assignDialog.height"
      @on-close="onAssignClose"
      @on-confirm="onAssignConfirm"
    >
      <template #content>
        <el-tree
          ref="assignTree"
          :data="assignTreeData"
          node-key="id"
          :props="defaultProps"
          empty-text="暂无数据"
          :show-checkbox="true"
          :highlight-current="true"
          default-expand-all
        />
      </template>
    </system-dialog>
  </el-main>
</template>

<script setup lang="ts">
import { useUserStore } from "@/store";
import roleApi from "@/api/role";
import SystemDialog from "@/components/system/SystemDialog.vue";
import { objCopy, leafUtils, confirm as myConfirm, resetForm } from "@/utils";
import {
  Delete,
  Plus,
  Search,
  Edit,
  Setting,
  RefreshRight,
} from "@element-plus/icons-vue";
import { ElTree, FormInstance } from "element-plus";
import { hasAuth } from "@/plugins/permission";

const store: any = useUserStore(); // 使用 Pinia store

const userId = computed(() => store.user.id);

const searchModel = reactive({
  roleName: "",
  pageNo: 1,
  pageSize: 10,
  userId: userId.value,
});

const roleList = ref<any>([]);
const tableHeight = ref(0);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);

const roleForm = ref<FormInstance>();

const roleDialog = reactive({
  title: "",
  visible: false,
  height: 230,
  width: 500,
});

const role = reactive({
  id: 0,
  roleCode: "",
  roleName: "",
  remark: "",
  createUser: userId.value,
});

const assignDialog = reactive({
  title: "",
  visible: false,
  height: 450,
  width: 300,
});

const roleId = ref("");
const assignTreeData = ref<any[]>([]);
const assignTree = ref<InstanceType<typeof ElTree>>();

const defaultProps = reactive({
  children: "children",
  label: "label",
});

const rules = reactive({
  roleCode: [{ required: true, trigger: "blur", message: "请输入角色编码" }],
  roleName: [{ required: true, trigger: "blur", message: "请输入角色名称" }],
});

const search = async (pageNo = 1, pageSize = 10) => {
  searchModel.pageNo = pageNo;
  searchModel.pageSize = pageSize;
  const { records, total: totalVal } = await roleApi.getRoleList(searchModel);

  roleList.value = records;
  total.value = totalVal;
};

onMounted(() => {
  watchEffect(() => {
    tableHeight.value = window.innerHeight - 220;
  });
  search();
});

const resetValue = () => {
  searchModel.roleName = "";
  search();
};

const openAddWindow = () => {
  resetForm(roleForm.value, role);
  role.createUser = userId.value;
  roleDialog.title = "新增角色";
  roleDialog.visible = true;
};

const onClose = () => {
  roleDialog.visible = false;
};

const onConfirm = async () => {
  roleForm.value?.validate(async (valid: any) => {
    if (valid) {
      let data = null;
      if (role.id === 0) {
        data = await roleApi.addRole(role);
      } else {
        data = await roleApi.updateRole(role);
      }
      ElMessage.success(data.message);
      window.location.reload();
      roleDialog.visible = false;
    }
  });
};

const handleSizeChange = (size: any) => {
  pageSize.value = size;
  search(pageNo.value, size);
};

const handleCurrentChange = (page: any) => {
  pageNo.value = page;
  search(page, pageSize.value);
};

const handleEdit = (row: any) => {
  console.log(row);

  objCopy(row, role);
  roleDialog.title = "编辑角色";
  roleDialog.visible = true;
};

const handleDelete = async (row: any) => {
  const confirm = await myConfirm("确定要删除该数据吗?");

  if (confirm) {
    const { message } = await roleApi.deleteById(row.id);
    ElMessage.success(message);
    search(pageNo.value, pageSize.value);
  }
};

const assignRole = async (row: any) => {
  roleId.value = row.id;
  const params = {
    roleId: row.id,
    userId: userId.value,
  };

  const { permissionList, checkList } = await roleApi.getAssignTreeApi(params);
  const { setLeaf } = leafUtils();
  const newPermissionList = setLeaf(permissionList);
  assignTreeData.value = newPermissionList;

  // 确保 DOM 更新完成后再执行操作
  await nextTick();

  // 尝试设置 checked keys
  setChild(assignTree.value?.data, checkList);

  assignDialog.visible = true;
  assignDialog.title = `给【${row.roleName}】分配权限`;
};

const setChild = (childNodes: any[], checkList: any[]) => {
  if (childNodes && childNodes.length > 0 && checkList.length > 0) {
    childNodes.forEach((node: any, index: any) => {
      // const treeNode = assignTree.value?.getNode(node);

      if (checkList.includes(node.id)) {
        // console.log("treeNode", treeNode);

        if ((node.children.length <= 0 && node.parentId > 0) || node.open) {
          // 判断是否为叶子节点 设置选中状态
          assignTree.value?.setChecked(node, true, false);
        }
      }
      if (node.children) {
        setChild(node.children, checkList);
      }
    });
  }
};

const onAssignClose = () => {
  assignDialog.visible = false;
};

const onAssignConfirm = async () => {
  const ids = ref([]);
  const pids = ref([]);
  // 获取选中的节点key和父节点id
  ids.value = assignTree.value?.getCheckedKeys();
  pids.value = assignTree.value?.getHalfCheckedKeys();
  const listId = [...ids.value, ...pids.value];
  if (listId.length <= 0) {
    ElMessage.warning("请选择权限");
    return;
  }
  const params = {
    roleId: roleId.value,
    list: listId,
  };
  const { message } = await roleApi.assignSaveApi(params);
  ElMessage.success(message);
  assignDialog.visible = false;
};
</script>

<style></style>
