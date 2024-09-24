// pages/message/message.js
import messageApi from "../../api/message/message";
import Message from 'tdesign-miniprogram/message/index';
const app = getApp(); // 获取全局对象
Page({
    data: {
        messages: [],
    },
    onLoad: function () {
        this.loadMessages();
    },
    onShow() {
        this.updateTabBar();
        if(app.globalData.needRefresh === true) {
            this.loadMessages();
            app.globalData.needRefresh = false;
        }
    },
    updateTabBar() {
        const activeTab = app.globalData.activeTab; // 获取全局变量tab状态
        if (typeof this.getTabBar === 'function') {
            this.getTabBar((tabBar) => {
                tabBar.setData({
                    currentTab: activeTab
                });
            });
        }
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    loadMessages: function () {
        messageApi.list().then(res => {
            if(res.success) {
                this.setData({messages: res.data});
            }
        }).catch(err => {
            console.error('加载消息失败:', err);
        }).finally(() => {
            wx.stopPullDownRefresh();
        });
    },
    goToChatPage(e) {
        const {item} = e.currentTarget.dataset;
        const info = {
            buyer: item.buyer,
            seller: item.seller,
            product: item.product,
            relationId: item.id
        }
        wx.navigateTo({
          url: `../chat/chat?info=${decodeURIComponent(JSON.stringify(info))}`,
        });
    },
    onPullDownRefresh: function () {
        this.loadMessages();
    },
})