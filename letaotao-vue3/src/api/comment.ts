import request from "@/utils/request";

const apiPrefix = "/api/comments";

class CommentApi {
  /**
   * 分页查询留言/评论列表
   * @param params
   */
  static async getCommentList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/page`, { params });
  }
  /**
   * 删除留言/评论
   */
  static async deleteCommentById(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
}

export default CommentApi;
