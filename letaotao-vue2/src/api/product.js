import http from "@/utils/request";

const apiPrefix = "/api/products";

export default {
  /**
   * 分页查询商品列表
   * @param params
   */
  async getProductList(params) {
    return await http.get(`${apiPrefix}/list/page`, params);
  },
  /**
   * 修改商品
   */
  async updateProduct(params) {
    return await http.put(`${apiPrefix}`, params);
  },
  /**
   * 批量修改商品
   */
  async batchUpdateProduct(params) {
    return await http.put(`${apiPrefix}/batch`, params);
  },
  /**
   * 删除商品
   */
  async deleteProductById(params) {
    return await http.delete(`${apiPrefix}`, params);
  },
};
