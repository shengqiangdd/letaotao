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
    ></el-option>
  </el-select>
</template>

<script>
import dict from "@/api/dict";

export default {
  props: {
    dictCode: {
      type: String,
      required: true,
    },
    value: {
      type: [String, Number],
      default: "",
    },
    placeholder: {
        type: String,
        default: '请选择'
    }
  },
  data() {
    return {
      selectedValue: this.value, // 默认选中的值
      options: [], // 数据字典选项列表
    };
  },
  watch: {
    value(newVal) {
      this.selectedValue = newVal;
    },
  },
  methods: {
    onSelectChange(value) {
      this.$emit("input", value);
    },
    fetchOptions() {
      dict
        .getDictListByDictCode(this.dictCode)
        .then((response) => {
          this.options = response.data;
          this.options.unshift({ value: "", label: "全部" })
        })
        .catch(() => {});
    },
  },
  mounted() {
    this.fetchOptions();
  },
};
</script>
