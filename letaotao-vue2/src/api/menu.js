import http from "@/utils/request"

const apiPrefix = "/api/permission";

export default {
    /**
     * 查询权限菜单列表
     * @param params
     * @returns
     */
    async getMenuList(params) {
        return await http.get(`${apiPrefix}/list`,params);
    },
    /**
     * 获取上级菜单
     * @param params
     * @returns
     */
    async getParentMenuList(params) {
        return await http.get(`${apiPrefix}/parent/list`,params);
    },
    /**
     * 添加菜单
     * @param params
     * @returns
     */
    async addMenu(params) {
        return await http.post(`${apiPrefix}`,params);
    },
    /**
     * 修改菜单
     * @param params
     * @returns
     */
    async updateMenu(params) {
        return await http.put(`${apiPrefix}`,params);
    },
    /**
     * 检查菜单下是否存在子菜单
     * @param params
     * @returns
     */
    async checkPermission(params) {
        return await http.getRestApi(`${apiPrefix}/check`,params);
    },
    /**
     * 删除菜单
     * @param params
     * @returns
     */
    async deleteById(params) {
        return await http.delete(`${apiPrefix}`,params);
    }
}
