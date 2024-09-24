import {
    put,
    post,
    get,
    del
} from '../../utils/request';

const apiPrefix = "/api/wx_posts";

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
    get(param) {
        const userId = wx.getStorageSync('userId');
        return get(`${apiPrefix}/${param}?userId=${userId}`, null, false);
    },
    update(params) {
        return put(`${apiPrefix}`, params);
    },
    delete(id) {
        return del(`${apiPrefix}/${id}`);
    },
}