// pages/search/search.js
import Message from 'tdesign-miniprogram/message/index';
Page({
    data: {
        historyList: [] // 存储搜索历史的数组
    },
    onLoad(options) {
        this.loadHistory();
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    onSearch: function (e) {
        let keyword = e.detail.value; // 搜索的关键词
        if (!keyword) return;

        let historyList = this.data.historyList;
        // 添加到搜索历史数组的首位
        if (historyList.indexOf(keyword) === -1) {
            historyList.unshift(keyword);
            if (historyList.length > 30) {
                historyList.pop(); // 保持搜索历史记录不超过10个
            }
        }

        wx.navigateTo({
            url: `../../pages/show-list/show-list?label=${keyword}`,
            success: () => {
                // 保存到本地存储
                wx.setStorageSync('searchHistory', historyList);
                this.setData({
                    historyList: historyList
                });
            }
        });
    },
    goToShowListPage(e) {
        const label = e.currentTarget.dataset.item;
        wx.navigateTo({
            url: `../show-list/show-list?label=${label}`,
        });
    },
    onDeleteTap: function (e) {
        let index = e.currentTarget.dataset.index;
        let historyList = this.data.historyList;
        historyList.splice(index, 1); // 删除所点击的历史记录
        this.setData({
            historyList: historyList
        });
        // 更新本地存储
        wx.setStorageSync('searchHistory', historyList);
    },
    loadHistory: function () {
        // 从本地存储加载搜索历史
        let historyList = wx.getStorageSync('searchHistory') || [];
        this.setData({
            historyList: historyList
        });
    }
})