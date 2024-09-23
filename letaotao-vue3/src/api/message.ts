import request from "@/utils/request";

const apiPrefix = "/api/messages";

class MessageApi {
  /**
   * 分页查询通知/消息列表
   * @param params
   */
  static async getMessageList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/page`, { params });
  }

  /**
   * 删除通知/消息
   * @param params
   */
  static async deleteMessageById(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
}

export default MessageApi;
