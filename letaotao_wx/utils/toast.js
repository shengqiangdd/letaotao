// utils/toast.js
const toast = (title, icon = 'none', duration = 1000, mask = false) => {
    return new Promise((resolve) => {
        wx.showToast({
            title: title,
            icon: icon,
            duration: duration,
            mask: mask,
            complete: () => {
                resolve();
            }
        });
    });
};

export default {
    show(title) {
        return toast(title);
    },
    success(title) {
        return toast(title, 'success');
    },
    error(title) {
        return toast(title, 'error');
    },
    loading(title) {
        return toast(title, 'loading', 5000, true);
    },
    hide() {
        wx.hideToast();
    },
    loadingWithNavigation(title, navigateFunc) {
        console.log('准备显示 loading 并执行 navigateFunc');
        this.loading(title); // 显示 loading 提示
        return new Promise((resolve, reject) => {
            navigateFunc().then(() => {
                console.log('navigateFunc 执行结束，准备隐藏 loading');
                this.hide(); // 在 navigateFunc 完成后关闭提示
                resolve(); // resolve 最外层的 Promise
            }).catch(error => {
                console.error('navigateFunc 执行失败:', error);
                this.hide(); // 隐藏 loading
                reject(error); // reject 最外层的 Promise
            });
        });
    }
};