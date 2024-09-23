import request from "@/utils/request";

const apiPrefix = "/api/lt_users";

class LTUserAPI {
  /**
   * 分页查询用户列表
   * @param params
   * @returns
   */
  static async getLtUserList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/page`, { params });
  }
  /**
   * 删除用户
   * @param params
   * @returns
   */
  static async deleteLtUser(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
}

export default LTUserAPI;
