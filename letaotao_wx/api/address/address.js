import {
    put,
    post,
    get,
    del
} from '../../utils/request';

const apiPrefix = "/api/wx_addresses";

export default {
    list(params) {
        return get(`${apiPrefix}/list`, params);
    },
    add(params) {
        return post(`${apiPrefix}`, params);
    },
    getDefault(userId) {
        return get(`${apiPrefix}/default/${userId}`);
    },
    update(params) {
        return put(`${apiPrefix}`, params);
    },
    delete(id) {
        return del(`${apiPrefix}/${id}`);
    },
}