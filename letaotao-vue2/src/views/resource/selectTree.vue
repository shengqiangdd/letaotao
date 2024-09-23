<template>
  <!-- 选择树弹框 -->
  <system-dialog
    :title="nodeDialog.title"
    :width="nodeDialog.width"
    :height="nodeDialog.height"
    :visible="nodeDialog.visible"
    @onClose="onWindowClose"
    @onConfirm="onWindowConfirm"
  >
    <div slot="content">
      <el-tree
        ref="nodeTree"
        :data="nodeList"
        default-expand-all
        node-key="id"
        :props="defaultProps"
        :show-checkbox="false"
        :highlight-current="true"
        :expand-on-click-node="false"
        @node-click="nodeClick"
      >
        <div class="customer-tree-node" slot-scope="{ node, data }">
          <span v-if="data.children.length == 0">
            <i class="el-icon-document" />
          </span>
          <span v-else @click.stop="openNodeBtn(data)">
            <svg-icon v-if="data.open" icon-class="add-s" />
            <svg-icon v-else icon-class="sub-s" />
          </span>
          <span style="margin-left: 3px">{{ node.label }}</span>
        </div>
      </el-tree>
    </div>
  </system-dialog>
</template>

<script>
export default {
  name: "selectTree",
  props: {
    nodeDialog: {
      //节点窗口
      type: Object,
      default: {
        title: "选择节点窗口",
        width: 300,
        height: 450,
        visible: false,
      },
    },
    defaultProps: {
      //节点属性
      type: Object,
      default: {
        children: "children",
        label: "name",
      },
    },
    nodeList: {
      //节点数据
      type: Array,
      default: [],
    },
  },
  data() {
    return {};
  },
};
</script>

<style></style>
