import http from "@/utils/request";

const apiPrefix = "/api/oss/file";

export default {
  /**
   * 文件上传
   * @param params
   */
  async upload(params) {
    return await http.post(`${apiPrefix}/upload`, params);
  },
  /**
   * 文件批量上传
   */
  async multiUpload(params) {
    return await http.post(`${apiPrefix}/multiUpload`, params);
  }
};
