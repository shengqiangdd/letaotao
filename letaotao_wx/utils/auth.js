// utils/auth.js
export const getCurrentPageUrlWithArgs = () => {
    const pages = getCurrentPages(); // 获取加载的页面数组
    const currentPage = pages[pages.length - 1]; // 获取当前页面
    const url = currentPage.route; // 当前页面URL
    const options = currentPage.options; // 当前页面参数
    // 将当前页面的URL和参数拼接成完整的URL
    let urlWithArgs = `/${url}?`;
    for (let key in options) {
        let value = options[key];
        urlWithArgs += `${key}=${value}&`;
    }
    urlWithArgs = urlWithArgs.substring(0, urlWithArgs.length - 1); // 移除最后一个多余的&
    // 使用encodeURIComponent对URL进行编码
    return encodeURIComponent(urlWithArgs);
};

export const redirectToLogin = () => {
    const currentPageUrlWithArgs = getCurrentPageUrlWithArgs();
    wx.navigateTo({
        url: `/pages/login/login?redirect=${currentPageUrlWithArgs}`
    });
};

export const checkLoginStatus = () => {
    const token = wx.getStorageSync('token');
    const tokenExpire = wx.getStorageSync('tokenExpire');

    if (!token || !tokenExpire || Date.now() >= new Date(tokenExpire).getTime()) {
        redirectToLogin();
        return false;
    }
    return true;
};

export const checkAndAuthorize = (scope, onSuccess, onFail) => {
    // 获取当前的授权状态
    wx.getSetting({
        success(res) {
            if (res.authSetting[scope]) {
                // 用户已授权
                onSuccess && onSuccess();
            } else {
                // 用户未选择，发起授权请求
                wx.authorize({
                    scope: scope,
                    success() {
                        // 用户点击同意授权
                        onSuccess && onSuccess();
                    },
                    fail() {
                        // 用户点击拒绝授权，引导用户进入小程序设置页
                        wx.showModal({
                            title: '请求授权',
                            content: '需要您授权才能进行下一步操作',
                            showCancel: true,
                            cancelText: '取消',
                            confirmText: '去设置',
                            success: function (modalRes) {
                                if (modalRes.confirm) {
                                    wx.openSetting({
                                        success(settingRes) {
                                            if (settingRes.authSetting[scope]) {
                                                // 用户在设置页选择同意授权
                                                onSuccess && onSuccess();
                                            } else {
                                                // 用户在设置页选择拒绝授权
                                                onFail && onFail();
                                            }
                                        }
                                    });
                                } else {
                                    onFail && onFail();
                                }
                            }
                        });
                    }
                });
            }
        },
        fail(err) {
            console.log(err);
            onFail && onFail();
        }
    });
};