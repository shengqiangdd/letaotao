import http from "@/utils/request";

const apiPrefix = "/api/statistics";

export default {
  /**
   * 查询统计数据
   * @param params
   */
  async get(params) {
    return await http.get(`${apiPrefix}/overview${params}`);
  }

};
