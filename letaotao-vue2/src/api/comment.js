import http from "@/utils/request";

const apiPrefix = "/api/comments";

export default {
  /**
   * 分页查询留言/评论列表
   * @param params
   */
  async getCommentList(params) {
    return await http.get(`${apiPrefix}/list/page`, params);
  },
  /**
   * 删除留言/评论
   */
  async deleteCommentById(params) {
    return await http.delete(`${apiPrefix}`, params);
  },
};
