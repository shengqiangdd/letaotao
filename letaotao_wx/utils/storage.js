// utils/storage.js
export const setStorage = (key, data) => {
    wx.setStorageSync(key, data);
};

/**
 * 设置存储在 localStorage 中对象的属性值。
 * @param {String} key 存储对象的键。
 * @param {String} property 要修改的属性名。
 * @param {any} value 要设置的新值。
 */
export const setStorageProperty = (key, property, value) => {
    try {
        // 尝试从 localStorage 中获取对象
        let storedObject = wx.getStorageSync(key);
        // 判断获取的数据是否为对象，如果不是，则初始化为空对象
        if (typeof storedObject !== 'object' || storedObject === null) {
            storedObject = {};
        }
        // 设置对象的属性
        storedObject[property] = value;
        // 将修改后的对象保存回 localStorage
        wx.setStorageSync(key, storedObject);
    } catch (error) {
        console.error('Error setting storage property:', error);
    }
};


export const getStorage = (key) => {
    return wx.getStorageSync(key);
};

export const removeStorage = (key) => {
    wx.removeStorageSync(key);
};

export const clearStorage = () => {
    wx.clearStorageSync();
};

export const clearUserInfo = () => {
    wx.removeStorageSync('token');
    wx.removeStorageSync('userId');
    wx.removeStorageSync('userInfo');
};