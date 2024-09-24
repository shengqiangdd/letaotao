import {
    post,
} from '../../utils/request';

const apiPrefix = "/api/wx_likes";

export default {
    addOrUpdate(params) {
        return post(`${apiPrefix}`, params);
    },
}