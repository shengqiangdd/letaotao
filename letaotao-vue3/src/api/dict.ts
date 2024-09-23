import request from "@/utils/request";

const apiPrefix = "/api/dictionaries";

class DictApi {
  // 获取字典列表
  static getDictList(params: any): Promise<any> {
    return request.get(`${apiPrefix}/list`, { params });
  }
  // 获取字典列表
  static getDictListByDictCode(params: any): Promise<any> {
    return request.get(`${apiPrefix}/list/code/${params}`);
  }
  // 导出字典
  static exportDict() {
    request({
      url: `${apiPrefix}/exportData`,
      method: "get",
      responseType: "blob",
    })
      .then((res) => {
        console.log(res);
        // 获取 Blob 数据
        const blob = new Blob([res.data], {
          type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        });

        // 创建一个 URL 对象指向 Blob
        const url = window.URL.createObjectURL(blob);

        // 创建一个新的窗口
        const popup = window.open(url, "_blank");

        popup?.addEventListener("load", function () {
          // 监听窗口关闭事件，清理 URL 对象
          popup?.close();
          window.URL.revokeObjectURL(url);
        });

        // 清理 URL 对象
        // window.URL.revokeObjectURL(url);

        // 关闭新窗口
        // popup.close();
      })
      .catch((err) => {
        console.log(err);
      });
  }
  // 导入字典
  static importDict(params: any): Promise<any> {
    return request.post(`${apiPrefix}/importData`, params);
  }
}

export default DictApi;
