import http from "@/utils/request";

const apiPrefix = "/api/lt_users";

export default {
  /**
   * 分页查询用户列表
   * @param params
   * @returns
   */
  async getLtUserList(params) {
    return await http.get(`${apiPrefix}/list/page`, params);
  },
  /**
   * 删除用户
   * @param params
   * @returns
   */
  async deleteLtUser(params) {
    return await http.delete(`${apiPrefix}`, params);
  },
};
