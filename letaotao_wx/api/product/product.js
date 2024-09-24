import {
    put,
    post,
    get,
    del
} from '../../utils/request';

const apiPrefix = "/api/wx_products";

export default {
    list(params) {
        return get(`${apiPrefix}/list/page`, params, false);
    },
    listByUserId(params) {
        return get(`${apiPrefix}/list/user/page`, params, false);
    },
    listByMe(params) {
        return get(`${apiPrefix}/list/cur_user/page`, params);
    },
    publish(params) {
        return post(`${apiPrefix}/publish`, params);
    },
    save(params) {
        return post(`${apiPrefix}/save`, params);
    },
    update(params) {
        return put(`${apiPrefix}`, params);
    },
    get(id) {
        const userId = wx.getStorageSync('userId');
        return get(`${apiPrefix}/${id}?userId=${userId}`, null, false);
    },
    delete(id) {
        return del(`${apiPrefix}/${id}`);
    },
    withdraw(params) {
        return put(`${apiPrefix}/withdraw`, params);
    },
    relist(params) {
        return put(`${apiPrefix}/relist`, params);
    }
}