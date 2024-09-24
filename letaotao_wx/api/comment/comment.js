import {
    post,
    get,
    del
} from '../../utils/request';

const apiPrefix = "/api/wx_comments";

export default {
    list(params) {
        return get(`${apiPrefix}/list`, params, false);
    },
    add(params) {
        return post(`${apiPrefix}`, params);
    },
    delete(id) {
        return del(`${apiPrefix}/${id}`);
    },
}