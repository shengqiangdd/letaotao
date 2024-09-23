import http from "@/utils/request";

const apiPrefix = "/api/orders";

export default {
  /**
   * 分页查询订单列表
   * @param params
   */
  async getOrderList(params) {
    return await http.get(`${apiPrefix}/list/page`, params);
  },
  /**
   * 修改订单
   */
  async updateOrder(params) {
    return await http.put(`${apiPrefix}`, params);
  },
  /**
   * 删除订单
   */
  async deleteOrderById(params) {
    return await http.delete(`${apiPrefix}`, params);
  },
};
