import {
    put,
    post,
    del
} from '../../utils/request';

const apiPrefix = "/api/lt_images";

export default {
    upload(params) {
        return post(`${apiPrefix}/upload`, params);
    },
    update(params) {
        return put(`${apiPrefix}`, params);
    },
    updateBatch(params) {
        return put(`${apiPrefix}/batch`, params);
    },
    delete(id) {
        return del(`${apiPrefix}/${id}`);
    }
}