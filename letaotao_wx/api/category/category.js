import {
    get
} from '../../utils/request';

const apiPrefix = "/api/wx_categories";

export default {
    getCategoryTreeList(params) {
        return get(`${apiPrefix}/tree`, params, false);
    }
}