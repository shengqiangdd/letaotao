import http from "@/utils/request";

const apiPrefix = "/api/categories";

export default {
  /**
   * 分页查询分类列表
   * @param params
   */
  async getCategoryPageList(params) {
    return await http.get(`${apiPrefix}/list/page`, params);
  },
    /**
   * 查询分类列表
   * @param params
   */
    async getCategoryList(params) {
      return await http.get(`${apiPrefix}/list`, params);
    },
  /**
   * 查询分类树列表
   * @param params
   */
  async getCategoryTreeList(params) {
    return await http.get(`${apiPrefix}/tree`, params);
  },
  /**
   * 添加分类
   */
  async addCategory(params) {
    return await http.post(`${apiPrefix}`, params);
  },
  /**
   * 修改分类
   */
  async updateCategory(params) {
    return await http.put(`${apiPrefix}`, params);
  },
  /**
   * 删除分类
   */
  async deleteCategoryById(params) {
    return await http.delete(`${apiPrefix}`, params);
  },
};
