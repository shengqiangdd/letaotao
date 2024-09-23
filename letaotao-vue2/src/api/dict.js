import http from "@/utils/request";

const apiPrefix = "/api/dictionaries";

export default {
  // 获取字典列表
  getDictList(params) {
    return http.get(`${apiPrefix}/list`, params);
  },
  // 获取字典列表
  getDictListByDictCode(params) {
    return http.get(`${apiPrefix}/list/code/${params}`);
  },
  // 导出字典
  exportDict() {
    return http.downLoad(`${apiPrefix}/exportData`, {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    });
  },
  // 导入字典
  importDict(params) {
    return http.post(`${apiPrefix}/importData`, params);
  },
};
