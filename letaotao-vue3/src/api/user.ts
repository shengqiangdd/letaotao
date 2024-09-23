import request from "@/utils/request";

const apiPrefix = "/api/sysUser";

class UserAPI {
  /**
   * 获取当前登录用户信息
   *
   * @returns 登录用户昵称、头像信息，包括角色和权限
   */
  static async getInfo() {
    return await request<any, UserInfo>({
      url: `${apiPrefix}/info`,
      method: "get",
    });
  }
  /**
   * 分页查询用户列表
   * @param params
   * @returns
   */
  static async getUserList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/page`, { params });
  }
  /**
   * 添加用户
   * @param params
   * @returns
   */
  static async addUser(params: any): Promise<any> {
    return await request.post(`${apiPrefix}`, params);
  }
  /**
   * 修改用户
   * @param params
   * @returns
   */
  static async updateUser(params: any): Promise<any> {
    return await request.put(`${apiPrefix}`, params);
  }
  /**
   * 删除用户
   * @param params
   * @returns
   */
  static async deleteUser(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
  /**
   * 查询用户角色列表
   * @param params
   * @returns
   */
  static async getAssignRoleList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/assign/role`, { params });
  }
  /**
   * 获取分配角色列表数据
   * @param params
   * @returns
   */
  static async getRoleIdByUserId(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/role/user/${params}`);
  }
  /**
   * 分配角色
   * @param params
   * @returns
   */
  static async assignRoleSave(params: any): Promise<any> {
    return await request.post(`${apiPrefix}/saveUserRole`, params);
  }
}

export default UserAPI;

/** 登录用户信息 */
export interface UserInfo {
  /** 用户ID */
  userId?: number;

  /** 用户名 */
  username?: string;

  /** 昵称 */
  nickname?: string;

  /** 头像URL */
  avatar?: string;

  /** 角色 */
  roles: string[];

  /** 权限 */
  permissions: string[];
}
