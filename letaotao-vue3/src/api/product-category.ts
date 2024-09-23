import request from "@/utils/request";

const apiPrefix = "/api/categories";

class ProductCategoryApi {
  /**
   * 分页查询分类列表
   * @param params
   */
  static async getCategoryList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/page`, { params });
  }
  /**
   * 查询分类树列表
   * @param params
   */
  static async getCategoryTreeList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/tree`, { params });
  }
  /**
   * 添加分类
   */
  static async addCategory(params: any): Promise<any> {
    return await request.post(`${apiPrefix}`, params);
  }
  /**
   * 修改分类
   */
  static async updateCategory(params: any): Promise<any> {
    return await request.put(`${apiPrefix}`, params);
  }
  /**
   * 删除分类
   */
  static async deleteCategoryById(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
}

export default ProductCategoryApi;
