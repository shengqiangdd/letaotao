// utils/request.js
import {
    redirectToLogin
} from './auth';
import {
    clearUserInfo
} from './storage';
const app = getApp();
const BASE_URL = app.globalData.baseUrl;

const request = (url, method, data, needToken = true) => {
    return new Promise((resolve, reject) => {
        const header = {
            'content-type': 'application/json'
        };
        if (needToken) {
            const token = wx.getStorageSync('token');
            if (token && token.length > 0) {
                header['token'] = `${token}`;
            } else {
                // 如果需要Token，但是Token不存在，直接重定向到登录页面
                return redirectToLogin();
            }
        }

        wx.request({
            url: `${BASE_URL}${url}`,
            method,
            data,
            header,
            success: (res) => {
                const {data} = res;
                if (data.code === 200) {
                    resolve(data);
                } else if (data.code === 40002 || data.code === 40003) {
                    // 当API返回40002时，统一重定向到登录页面
                    clearUserInfo();
                    redirectToLogin();
                    reject(new Error('未授权或Token过期'));
                } else {
                    reject(new Error(res.data.message || '请求失败'));
                }
            },
            fail: (error) => {
                reject(new Error('网络错误或请求失败：' + error.errMsg));
            },
        });
    });
};

export const get = (url, data, needToken = true) => request(url, 'GET', data, needToken);
export const post = (url, data, needToken = true) => request(url, 'POST', data, needToken);
export const put = (url, data, needToken = true) => request(url, 'PUT', data, needToken);
export const del = (url, data, needToken = true) => request(url, 'DELETE', data, needToken);