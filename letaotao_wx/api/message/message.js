import {
    get,
} from '../../utils/request';

const apiPrefix = "/api/wx_messages";

export default {
    list(param) {
        return get(`${apiPrefix}/list`, param);
    }
}