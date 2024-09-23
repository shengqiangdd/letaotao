import request from "@/utils/request";

const apiPrefix = "/api/role";

class RoleApi {
  /**
   * 分页查询角色列表
   * @param params
   */
  static async getRoleList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/page`, { params });
  }
  static async getAssignTreeApi(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/assign/permission/tree`, {
      params,
    });
  }
  /**
   * 添加角色
   */
  static async addRole(params: any): Promise<any> {
    return await request.post(`${apiPrefix}`, params);
  }
  /**
   * 修改角色
   */
  static async updateRole(params: any): Promise<any> {
    return await request.put(`${apiPrefix}`, params);
  }
  /**
   * 删除角色
   */
  static async deleteById(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
  /**
   * 分配权限
   */
  static async assignSaveApi(params: any): Promise<any> {
    return await request.post(`${apiPrefix}/saveRoleAssign`, params);
  }
}

export default RoleApi;
