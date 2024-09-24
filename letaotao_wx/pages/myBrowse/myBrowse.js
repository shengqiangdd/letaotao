// pages/myBrowse/myBrowse.js
import Message from 'tdesign-miniprogram/message/index';
import toast from '../../utils/toast';
Page({
    data: {
        browsingData: []
    },
    onLoad(options) {
        this.setData({
            browsingData: wx.getStorageSync('browsingSession')
        });
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    onClearHistory: function () {
        if (this.data.browsingData.length <= 0) {
            return toast.show('没有数据要清空！');
        }
        wx.showModal({
            title: '确认清空',
            content: '您是否要清空所有浏览数据？',
            complete: (res) => {
                if (res.confirm) {
                    wx.setStorageSync('browsingSession', []);
                    // 更新视图数据
                    this.setData({
                        browsingData: []
                    });
                }
            }
        });

    },
    goToProductDetail: function (event) {
        const productId = event.currentTarget.dataset.id;
        wx.navigateTo({
            url: `../product-detail/product-detail?id=${productId}`,
        });
    },
})