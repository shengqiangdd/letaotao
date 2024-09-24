import {
    get,
    post,
    put
} from '../../utils/request';
const apiPrefix = "/api/wx_user";

export default {
    // 登录接口是不需要检查登录状态的
    login(params) {
        return post(`${apiPrefix}/login`, params, false);
    },
    list(params) {
        return get(`${apiPrefix}/list/page`, params, false);
    },
    get(userId) {
        return get(`${apiPrefix}/${userId}`, null, false);
    },
    followList(params) {
        return get(`${apiPrefix}/list/follow`, params, false);
    },
    follow(param) {
        return post(`${apiPrefix}/follow`, param);
    },
    getUserFollowCount(userId) {
        return get(`${apiPrefix}/followCount/${userId}`);
    },
    updateInfo(params) {
        return put(`${apiPrefix}/updateInfo`, params);
    },
    logout() {
        return post(`${apiPrefix}/logout`);
    },
}