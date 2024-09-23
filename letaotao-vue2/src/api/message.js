import http from "@/utils/request";

const apiPrefix = "/api/messages";

export default {
  /**
   * 分页查询通知/消息列表
   * @param params
   */
  async getMessageList(params) {
    return await http.get(`${apiPrefix}/list/page`, params);
  },
  /**
   * 删除通知/消息
   * @param param
   */
  async deleteMessageById(params) {
    return await http.delete(`${apiPrefix}`, params);
  },
};
