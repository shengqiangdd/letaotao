import request from "@/utils/request";

const apiPrefix = "/api/statistics";

class statisticsApi {
  /**
   * 查询统计数据
   * @param params
   */
  static async get(params: any) {
    return await request.get(`${apiPrefix}/overview${params}`);
  }
}

export default statisticsApi;
