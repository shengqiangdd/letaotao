import request from "@/utils/request";

const apiPrefix = "/api/posts";

class PostApi {
  /**
   * 分页查询帖子列表
   * @param params
   */
  static async getPostList(params: any): Promise<any> {
    return await request.get(`${apiPrefix}/list/page`, { params });
  }
  /**
   * 修改帖子
   */
  static async updatePost(params: any): Promise<any> {
    return await request.put(`${apiPrefix}`, params);
  }
  /**
   * 批量修改帖子
   */
  static async batchUpdatePost(params: any): Promise<any> {
    return await request.put(`${apiPrefix}/batch`, params);
  }
  /**
   * 删除帖子
   */
  static async deletePostById(params: any): Promise<any> {
    return await request.delete(`${apiPrefix}/${params}`);
  }
}

export default PostApi;
