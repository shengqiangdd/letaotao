<template>
  <div>
    <el-dialog
      top="5vh"
      :title="title"
      v-model="currentVisible"
      :width="`${width}px`"
      :before-close="onClose"
      :close-on-click-modal="false"
    >
      <div class="container" :style="{ height: `${height}px` }">
        <slot name="content"></slot>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="onClose">取 消</el-button>
          <el-button type="primary" @click="onConfirm">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
const props = defineProps({
  title: {
    type: String,
    default: "标题",
  },
  visible: {
    type: Boolean,
    default: false,
  },
  width: {
    type: Number,
    default: 600,
  },
  height: {
    type: Number,
    default: 250,
  },
});

const emit = defineEmits(["onClose", "onConfirm"]);

const currentVisible = ref(props.visible);

const onClose = () => {
  emit("onClose");
};

const onConfirm = () => {
  emit("onConfirm");
};

// 监听 props.visible 的变化，并同步更新 currentVisible
watch(
  () => props.visible,
  (newValue) => {
    currentVisible.value = newValue;
  }
);
</script>

<script>
export default {
  name: "SystemDialog",
};
</script>

<style lang="scss" scoped>
.container {
  overflow-x: initial;
  overflow-y: auto;
}

.el-dialog__wrapper {
  :deep(.el-dialog) {
    border-top-left-radius: 7px !important;
    border-top-right-radius: 7px !important;
    .el-dialog__header {
      border-top-left-radius: 7px !important;
      border-top-right-radius: 7px !important;
      background-color: #1894ff;
      .el-dialog__title {
        color: #fff;
        font-size: 15px;
        font-weight: 700;
      }
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__body {
      padding: 10px 10px !important;
    }
    .el-dialog__footer {
      border-top: 1px solid #e8eaec !important;
      padding: 10px !important;
    }
  }
}
</style>
