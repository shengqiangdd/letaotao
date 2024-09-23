<template>
  <el-main>
    <!-- 新增按钮 -->
    <el-button type="success" :icon="Plus" @click="openAddWindow">
      新增
    </el-button>
    <!-- 数据表格 -->
    <el-table
      style="margin-top: 10px"
      :height="tableHeight"
      :data="menuList"
      row-key="id"
      :default-expand-all="false"
      :tree-props="{ children: 'children' }"
      :border="true"
      stripe
    >
      <el-table-column prop="label" label="菜单名称" />
      <el-table-column prop="type" label="菜单类型" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.type == '0'" size="default">目录</el-tag>
          <el-tag
            type="success"
            v-else-if="scope.row.type == '1'"
            size="default"
          >
            菜单
          </el-tag>
          <el-tag
            type="warning"
            v-else-if="scope.row.type == '2'"
            size="default"
          >
            按钮
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="图标" align="center">
        <template #default="{ row }">
          <el-icon>
            <component :is="currentIconComponent(row)" />
          </el-icon>
          <!-- <i
            :class="scope.row.icon"
            v-if="scope.row.icon.includes('el-icon')"
          ></i>
          <svg-icon v-else :icon-class="scope.row.icon" /> -->
        </template>
      </el-table-column>
      <el-table-column prop="name" label="路由名称" />
      <el-table-column prop="path" label="路由地址" />
      <el-table-column prop="url" label="组件路径" />
      <el-table-column align="center">
        <template #default="scope">
          <el-button
            type="primary"
            :icon="Edit"
            size="small"
            @click="editMenu(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            type="danger"
            size="small"
            :icon="Delete"
            @click="deleteMenu(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增或编辑弹框 -->
    <system-dialog
      :title="menuDialog.title"
      :width="menuDialog.width"
      :height="menuDialog.height"
      :visible="menuDialog.visible"
      @on-close="onClose"
      @on-confirm="onConfirm"
    >
      <template #content>
        <el-form
          :model="menu"
          ref="menuForm"
          :rules="rules"
          label-width="80px"
          :inline="true"
          size="small"
        >
          <el-row>
            <el-col :span="24">
              <el-form-item prop="type" label="菜单类型">
                <el-radio-group v-model="menu.type">
                  <el-radio :value="0">目录</el-radio>
                  <el-radio :value="1">菜单</el-radio>
                  <el-radio :value="2">按钮</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item prop="parentName" size="small" label="所属菜单">
            <el-input
              @click="selectParentMenu"
              v-model="menu.parentName"
              :readonly="true"
            />
          </el-form-item>
          <el-form-item prop="label" size="small" label="菜单名称">
            <el-input v-model="menu.label" />
          </el-form-item>
          <el-form-item size="small" label="菜单图标">
            <my-icon @selection="setIcon" ref="iconRef" />
          </el-form-item>
          <el-form-item
            prop="name"
            v-if="menu.type == 1"
            size="small"
            label="路由名称"
          >
            <el-input v-model="menu.name" />
          </el-form-item>
          <el-form-item
            prop="path"
            v-if="menu.type != 2"
            size="small"
            label="路由地址"
          >
            <el-input v-model="menu.path" />
          </el-form-item>
          <el-form-item
            prop="url"
            v-if="menu.type == 1"
            size="small"
            label="组件路径"
          >
            <el-input v-model="menu.url" />
          </el-form-item>
          <el-form-item prop="code" size="small" label="权限字段">
            <el-input v-model="menu.code" />
          </el-form-item>
          <el-form-item size="small" label="菜单序号">
            <el-input v-model="menu.orderNum" />
          </el-form-item>
        </el-form>
      </template>
    </system-dialog>
    <!-- 选择所属菜单弹框 -->
    <system-dialog
      :title="parentDialog.title"
      :width="parentDialog.width"
      :height="parentDialog.height"
      :visible="parentDialog.visible"
      @on-close="onParentClose"
      @on-confirm="onParentConfirm"
    >
      <template #content>
        <el-tree
          style="font-size: 14px"
          ref="parentTree"
          :data="parentMenuList"
          node-key="id"
          :props="defaultProps"
          empty-text="暂无数据"
          :show-checkbox="false"
          default-expand-all
          :highlight-current="true"
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        >
          <template #default="{ node, data }">
            <div class="customer-tree-node">
              <!-- 长度为0说明没有下级 -->
              <span v-if="data.children.length == 0">
                <i
                  class="el-icon-document"
                  style="color: #8c8c8c; font-size: 18px"
                ></i>
              </span>
              <span v-else @click.stop="openBtn(data)">
                <svg-icon v-if="data.open" icon-class="add-s" />
                <svg-icon v-else icon-class="sub-s" />
              </span>
              <span style="margin-left: 3px">{{ node.label }}</span>
            </div>
          </template>
        </el-tree>
      </template>
    </system-dialog>
  </el-main>
</template>

<script setup lang="ts">
import menuApi from "@/api/menu";
import SystemDialog from "@/components/system/SystemDialog.vue";
import MyIcon from "@/components/system/MyIcon.vue";
import { objCopy, confirm as myConfirm, resetForm } from "@/utils";
import {
  Delete,
  Plus,
  Search,
  Edit,
  Setting,
  RefreshRight,
} from "@element-plus/icons-vue";
import type { FormInstance } from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

const menuList = ref<any[]>([]); //菜单列表
const tableHeight = ref(0); //表格高度

const menuForm = ref<FormInstance>();
const parentTree = ref<any>();
const iconRef = ref<any>();

const currentIconComponent = (row: any) => {
  return ElementPlusIconsVue[row.icon] || null;
};

const menuDialog = reactive({
  title: "",
  width: 630,
  height: 270,
  visible: false,
}); //新增或编辑弹框属性

const menu = reactive({
  id: 0,
  type: "",
  parentId: "",
  parentName: "",
  label: "",
  icon: "",
  name: "",
  path: "",
  url: "",
  code: "",
  orderNum: "",
}); //菜单属性

const parentDialog = reactive({
  title: "选择所属菜单",
  width: 280,
  height: 450,
  visible: false,
}); //上级菜单弹框列表

const defaultProps = reactive({
  children: "children",
  label: "label",
}); //树属性定义
const parentMenuList = ref<any>([]); //所属菜单列表
const rules = reactive({
  type: [{ required: true, trigger: "change", message: "请选择菜单类型" }],
  parentName: [
    { required: true, trigger: "change", message: "请选择所属菜单" },
  ],
  label: [{ required: true, trigger: "blur", message: "请输入菜单名称" }],
  name: [{ required: true, trigger: "blur", message: "请输入路由名称" }],
  path: [{ required: true, trigger: "blur", message: "请输入路由路径" }],
  url: [{ required: true, trigger: "blur", message: "请输入组件路径" }],
  code: [{ required: true, trigger: "blur", message: "请输入权限字段" }],
});

// 初始化时调用
onMounted(() => {
  nextTick(() => {
    tableHeight.value = window.innerHeight - 180;
  });
  search();
});

// 查询菜单列表
const search = async () => {
  let data = await menuApi.getMenuList();
  menuList.value = data;
};

// 打开添加窗口
const openAddWindow = () => {
  menuDialog.title = "新增菜单";
  menuDialog.visible = true;
  nextTick(() => {
    iconRef?.value?.setUserChooseIcon(""); //清空菜单图标
  });
};

// 设置图标
const setIcon = (icon: any) => {
  menu.icon = icon;
};

// 编辑菜单
const editMenu = (row: any) => {
  // 把当前要编辑的数据复制到数据域，给表单回显
  objCopy(row, menu);
  menuDialog.title = "编辑菜单";
  menuDialog.visible = true;
  nextTick(() => {
    iconRef?.value?.setUserChooseIcon(row.icon); //菜单图标回显
  });
};

// 删除菜单
const deleteMenu = async (row: any) => {
  await menuApi.checkPermission(row.id);
  let confirm = await myConfirm("确定要删除该数据吗?");
  if (confirm) {
    let data = await menuApi.deleteById(row.id);
    ElMessage.success(data.message);
    search();
  }
};

// 添加和修改菜单窗口关闭操作
const onClose = () => {
  menuDialog.visible = false;
};

// 添加和修改菜单窗口确认事件
const onConfirm = async () => {
  menuForm.value?.validate(async (valid: any) => {
    if (valid) {
      let data = null;
      //判断菜单ID是否为空 为空则添加，否则修改
      console.log(menu);

      if (menu.id === 0) {
        data = await menuApi.addMenu(menu);
      } else {
        data = await menuApi.updateMenu(menu);
      }
      ElMessage.success(data.message);
      // search();
      location.reload(); //关闭窗口
      menuDialog.visible = false;
    }
  });
};

// 选择所属菜单
const selectParentMenu = async () => {
  parentDialog.visible = true;
  let data: any = await menuApi.getParentMenuList();
  parentMenuList.value = data;
};

// 选择所属菜单取消事件
const onParentClose = () => {
  parentDialog.visible = false;
};

const onParentConfirm = () => {
  parentDialog.visible = false;
};

// 切换图标
const openBtn = (data: any) => {
  data.open = !data.open;
  parentTree.value.store.nodesMap[data.id].expanded = !data.open;
};

// 所属菜单节点点击事件
const handleNodeClick = (data: any) => {
  menu.parentId = data.id;
  menu.parentName = data.label;
};
</script>

<style lang="scss" scoped></style>
