import request from "@/utils/request";

const apiPrefix = "/api/products";

class ProductApi {
  /**
   * 分页查询商品列表
   * @param params
   */
  static async getProductList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/page`, { params });
  }
  /**
   * 修改商品
   */
  static async updateProduct(params: any): Promise<any> {
    return await request.put(`${apiPrefix}`, params);
  }
  /**
   * 批量修改商品
   */
  static async batchUpdateProduct(params: any): Promise<any> {
    return await request.put(`${apiPrefix}/batch`, params);
  }
  /**
   * 删除商品
   */
  static async deleteProductById(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
}

export default ProductApi;
