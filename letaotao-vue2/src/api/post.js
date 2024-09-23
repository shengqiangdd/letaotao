import http from "@/utils/request";

const apiPrefix = "/api/posts";

export default {
  /**
   * 分页查询帖子列表
   * @param params
   */
  async getPostList(params) {
    return await http.get(`${apiPrefix}/list/page`, params);
  },
  /**
   * 修改帖子
   */
  async updatePost(params) {
    return await http.put(`${apiPrefix}`, params);
  },
  /**
   * 批量修改帖子
   */
  async batchUpdatePost(params) {
    return await http.put(`${apiPrefix}/batch`, params);
  },
  /**
   * 删除帖子
   */
  async deletePostById(params) {
    return await http.delete(`${apiPrefix}`, params);
  },
};
