import {
    get,
    post
} from '../../utils/request';

const apiPrefix = "/api/wx_evaluates";

export default {
    listByUserId(userId) {
        return get(`${apiPrefix}/list/user/${userId}`, null, false);
    },
    listByOrderId(orderId) {
        return get(`${apiPrefix}/list/order/${orderId}`, null, false);
    },
    evaluate(params) {
        return post(`${apiPrefix}`, params);
    },
    isEvaluate(params) {
        return get(`${apiPrefix}/isEvaluate`, params);
    },
}