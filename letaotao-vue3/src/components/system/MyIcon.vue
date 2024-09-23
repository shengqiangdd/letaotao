<template>
  <div class="chooseIcons">
    <el-popover placement="bottom" width="450" trigger="click">
      <template #reference>
        <span
          style="
            display: inline-block;
            width: 200px;
            height: 33px;
            line-height: 33px;
          "
        >
          <component :is="currentComponent" class="icon-preview" />
          {{ userChooseIcon }}
        </span>
      </template>
      <div class="iconList">
        <component
          v-for="item in iconList"
          :key="item"
          :is="item"
          class="icon-item"
          @click="setIcon(item)"
          style="font-size: 20px"
        />
      </div>
    </el-popover>
  </div>
</template>

<script setup>
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

const userChooseIcon = ref("");
const iconList = ref([]);

const currentComponent = computed((n) => {
  return ElementPlusIconsVue[userChooseIcon.value] || null;
});

const setIcon = (icon) => {
  userChooseIcon.value = icon;
  emit("selection", icon);
};

const getIconList = () => {
  iconList.value = Object.keys(ElementPlusIconsVue);
};

const setUserChooseIcon = (icon) => {
  userChooseIcon.value = icon;
};

defineExpose({
  setUserChooseIcon,
});

const emit = defineEmits(["selection"]);

onMounted(() => {
  getIconList();
});
</script>

<script>
export default {
  name: "MyIcon",
};
</script>

<style lang="scss" scoped>
.iconList {
  width: 400px;
  height: 300px;
  overflow-x: hidden;
  overflow-y: scroll;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}

.icon-item {
  display: inline-block;
  width: 60px;
  height: 45px;
  color: #000000;
  font-size: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  text-align: center;
  line-height: 45px;
  margin: 5px;
  cursor: pointer;
  &:hover {
    color: orange;
    border-color: orange;
  }
}

.chooseIcons {
  width: 175px;
  background-color: #ffffff;
  background-image: none;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  box-sizing: border-box;
  color: #606266;
  display: inline-block;
  font-size: inherit;
  height: 33px;
  outline: none;
  padding: 0 15px;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
}

.icon-preview {
  display: inline-block;
  width: 20px;
  height: 20px;
  vertical-align: middle;
}
</style>
