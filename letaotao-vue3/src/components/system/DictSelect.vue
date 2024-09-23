<template>
  <el-select
    v-model="selectedValue"
    :placeholder="placeholder"
    @change="onSelectChange"
  >
    <el-option
      v-for="option in options"
      :key="option.value"
      :label="option.label"
      :value="option.value"
    />
  </el-select>
</template>

<script setup>
import dict from "@/api/dict";

const props = defineProps({
  dictCode: {
    type: String,
    required: true,
  },
  modelValue: {
    type: [String, Number],
    default: "",
  },
  placeholder: {
    type: String,
    default: "请选择",
  },
});

const emit = defineEmits(["update:modelValue"]);

const selectedValue = ref(props.modelValue); // 默认选中的值
const options = ref([]); // 数据字典选项列表

const onSelectChange = (value) => {
  emit("update:modelValue", value);
};

const fetchOptions = async () => {
  try {
    const data = await dict.getDictListByDictCode(props.dictCode);

    options.value = data;
    options.value.unshift({ value: "", label: "全部" });
  } catch (error) {
    console.error("Failed to fetch options:", error);
  }
};

// 监听父组件传递过来的 modelValue 属性变化，并更新 selectedValue
watch(
  () => props.modelValue,
  (newVal) => {
    selectedValue.value = newVal;
  },
  { immediate: true } // 立即执行一次，确保初始值同步
);

onMounted(() => {
  fetchOptions();
});
</script>

<script>
export default {
  name: "DictSelect",
};
</script>
