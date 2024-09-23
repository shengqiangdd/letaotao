import request from "@/utils/request";

const apiPrefix = "/api/orders";

class OrderApi {
  /**
   * 分页查询订单列表
   * @param params
   */
  static async getOrderList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/page`, { params });
  }
  /**
   * 修改订单
   */
  static async updateOrder(params: any): Promise<any> {
    return await request.put(`${apiPrefix}`, params);
  }
  /**
   * 删除订单
   */
  static async deleteOrderById(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
}

export default OrderApi;
