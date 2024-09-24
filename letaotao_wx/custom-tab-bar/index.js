// custom-tab-bar/index.js
const app = getApp(); // 获取全局对象
Component({
    data: {
        currentTab: 0, // 当前激活的tab索引
        tabs: [{
                pagePath: "pages/index/index",
                text: "首页",
                icon: "home",
                selectedIcon: "home-fill"
            },
            {
                pagePath: "pages/community/community",
                text: "社区",
                icon: "compass",
                selectedIcon: "compass-fill"
            },
            {
                text: "发布",
                isSpecial: true,
                pagePath: "pages/publish/publish",
            },
            {
                pagePath: "pages/message/message",
                text: "消息",
                icon: "message",
                selectedIcon: "message-fill"
            },
            {
                pagePath: "pages/profile/profile",
                text: "我的",
                icon: "user",
                selectedIcon: "user-fill"
            }
        ]
    },
    methods: {
        changeTab(e) {
            const idx = e.currentTarget.dataset.index;
            const tab = this.data.tabs[idx];
            if (!tab.isSpecial) {
                this.setData({
                    currentTab: idx
                }, () => {
                    app.globalData.activeTab = idx;
                    wx.switchTab({
                        url: '/' + tab.pagePath
                    });
                });
            } else {
                // 处理特殊按钮点击事件
                wx.navigateTo({
                    url: '/' + tab.pagePath
                });
            }
        }
    }
});