import http from "@/utils/request";

const apiPrefix = "/api/role";

export default {
  /**
   * 分页查询角色列表
   * @param params
   */
  async getRoleList(params) {
    return await http.get(`${apiPrefix}/list/page`, params);
  },
  async getAssignTreeApi(params) {
    return await http.get(`${apiPrefix}/assign/permission/tree`, params);
  },
  /**
   * 添加角色
   */
  async addRole(params) {
    return await http.post(`${apiPrefix}`, params);
  },
  /**
   * 修改角色
   */
  async updateRole(params) {
    return await http.put(`${apiPrefix}`, params);
  },
  /**
   * 删除角色
   */
  async deleteById(params) {
    return await http.delete(`${apiPrefix}`, params);
  },
  /**
   * 分配权限
   */
  async assignSaveApi(params) {
    return await http.post(`${apiPrefix}/saveRoleAssign`, params);
  },
};
