import {
    post,
} from '../../utils/request';

const apiPrefix = "/api/oss/file";

export default {
    /**
     * 文件上传
     * @param param
     */
    upload(params) {
        return post(`${apiPrefix}/upload`, params);
    },
    /**
     * 文件批量上传
     */
    multiUpload(params) {
        return post(`${apiPrefix}/multiUpload`, params);
    }
};