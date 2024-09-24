import {
    put,
    post,
    get,
    del
} from '../../utils/request';

const apiPrefix = "/api/wx_orders";

export default {
    list(params) {
        return get(`${apiPrefix}/list/page`, params);
    },
    getOrderById(id,userId) {
        if(userId) {
            return get(`${apiPrefix}/${id}?currentUserId=${userId}`,null,false);
        } else {
            return get(`${apiPrefix}/${id}`,null,false);
        }
    },
    get(params) {
        return get(`${apiPrefix}/chat`, params);
    },
    create(params) {
        return post(`${apiPrefix}`, params);
    },
    checkAvailable(productId) {
        return get(`${apiPrefix}/checkAvailable/${productId}`);
    },
    pay(params) {
        return put(`${apiPrefix}/pay`, params);
    },
    deliver(params) {
        return put(`${apiPrefix}/deliver`, params);
    },
    receive(params) {
        return put(`${apiPrefix}/receive`, params);
    },
    cancel(params) {
        return put(`${apiPrefix}/cancel`, params);
    },
    delete(id) {
        return del(`${apiPrefix}/${id}`);
    }
}