import request from "@/utils/request";

const userApiPrefix = "/api/sysUser";
const apiPrefix = "/api/permission";

class MenuAPI {
  /**
   * 获取当前用户的路由列表
   * <p/>
   * 无需传入角色，后端解析token获取角色自行判断是否拥有路由的权限
   *
   * @returns 路由列表
   */
  static async getRoutes() {
    return await request<any, RouteVO[]>({
      url: `${userApiPrefix}/menu`,
      method: "get",
    });
  }
  /**
   * 查询权限菜单列表
   * @param params
   * @returns
   */
  static async getMenuList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list`, { params });
  }
  /**
   * 获取上级菜单
   * @param params
   * @returns
   */
  static async getParentMenuList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/parent/list`, { params });
  }
  /**
   * 添加菜单
   * @param params
   * @returns
   */
  static async addMenu(params: any): Promise<any> {
    return await request.post(`${apiPrefix}`, params);
  }
  /**
   * 修改菜单
   * @param params
   * @returns
   */
  static async updateMenu(params: any): Promise<any> {
    return await request.put(`${apiPrefix}`, params);
  }
  /**
   * 检查菜单下是否存在子菜单
   * @param params
   * @returns
   */
  static async checkPermission(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/check/${params}`);
  }
  /**
   * 删除菜单
   * @param params
   * @returns
   */
  static async deleteById(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
}

export default MenuAPI;

/** RouteVO，路由对象 */
export interface RouteVO {
  /** 子路由列表 */
  children: RouteVO[];
  /** 组件路径 */
  component?: string;
  /** 路由属性 */
  meta?: Meta;
  /** 路由名称 */
  name?: string;
  /** 路由路径 */
  path?: string;
  /** 跳转链接 */
  redirect?: string;
}

/** Meta，路由属性 */
export interface Meta {
  /** 【目录】只有一个子路由是否始终显示 */
  alwaysShow?: boolean;
  /** 是否隐藏(true-是 false-否) */
  hidden?: boolean;
  /** ICON */
  icon?: string;
  /** 【菜单】是否开启页面缓存 */
  keepAlive?: boolean;
  /** 路由title */
  title?: string;
}
