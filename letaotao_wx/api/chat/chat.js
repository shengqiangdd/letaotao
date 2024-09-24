import {
    post,
    get,
} from '../../utils/request';

const apiPrefix = "/api/wx_chat";

export default {
    relation(param) {
        return get(`${apiPrefix}/relation`, param);
    },
    messages(param) {
        return get(`${apiPrefix}/messages/${param}`);
    },
    read(param) {
        return post(`${apiPrefix}/read`, param);
    }
}