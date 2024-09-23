import request from "@/utils/request";

const apiPrefix = "/api/oss/file";

class OssFileApi {
  /**
   * 文件上传
   * @param params
   */
  static async upload(params: any): Promise<any> {
    return await request.post(`${apiPrefix}/upload`, params);
  }
  /**
   * 文件批量上传
   */
  static async multiUpload(params: any): Promise<any> {
    return await request.post(`${apiPrefix}/multiUpload`, params);
  }
}

export default OssFileApi;
