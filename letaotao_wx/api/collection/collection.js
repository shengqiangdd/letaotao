import {
    post,
    get,
    del
} from '../../utils/request';

const apiPrefix = "/api/wx_collections";

export default {
    addOrUpdate(params) {
        return post(`${apiPrefix}`, params);
    },
    list(param) {
        return get(`${apiPrefix}/list`, param);
    },
    cancel(param) {
        return del(`${apiPrefix}/cancel`, param);
    },
}