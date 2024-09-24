// pages/profile/profile.js
import {
    getCurrentPageUrlWithArgs
} from '../../utils/auth';
import userApi from '../../api/user/user';
import {
    setStorageProperty
} from '../../utils/storage'
import Message from 'tdesign-miniprogram/message/index';
const app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        userInfo: {
            nickName: '点击登录',
            avatar: '',
        }
    },
    onLoad(options) {

    },
    onShow() {
        const activeTab = app.globalData.activeTab; // 获取全局变量tab状态
        if (typeof this.getTabBar === 'function') {
            this.getTabBar((tabBar) => {
                tabBar.setData({
                    currentTab: activeTab
                });
            });
        }
        const userInfoStore = wx.getStorageSync('userInfo');
 
        const {userInfo} = this.data;
        if (userInfo.nickName === '点击登录' && userInfoStore) {
            // 如果用户已登录，则执行当前页面的初始化逻辑
            this.initPage(userInfo);
        }
        if (userInfoStore) {
            if(userInfo.avatar !== userInfoStore.avatar || userInfo.nickName !== userInfoStore.nickName) {
                this.setData({userInfo: userInfoStore})
            }
            this.getUserFollowCount(userInfo);
        }
    },
    initPage(userInfo) {
        this.setData({
            'userInfo.nickName': userInfo.nickName,
            'userInfo.avatar': userInfo.avatar,
            'userInfo.followerCount': userInfo.followerCount,
            'userInfo.followingCount': userInfo.followingCount
        });
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    getUserFollowCount(userInfo) {
        if(!userInfo.id) {
            return;
        }
        userApi.getUserFollowCount(userInfo.id).then(res => {
            if (res.success) {
                let {
                    followerCount,
                    followingCount
                } = res.data;
                if (userInfo.followerCount !== followerCount || userInfo.followingCount !== followingCount) {
                    setStorageProperty('userInfo', 'followerCount', followerCount);
                    setStorageProperty('userInfo', 'followingCount', followingCount);
                    this.setData({
                        'userInfo.followerCount': followerCount,
                        'userInfo.followingCount': followingCount,
                    });
                }
            }
        });
    },
    goSetting() {
        // 导航到设置页面的代码
        wx.navigateTo({
            url: '../settings/settings',
        });
    },
    goLogin() {
        const {
            userInfo
        } = this.data;

        if (userInfo && userInfo.nickName != '点击登录') {
            // 用户已登录，无需重定向到登录页面
            return;
        }
        // 如果未登录，则跳转至登录页面
        const currentPage = getCurrentPageUrlWithArgs();
        wx.navigateTo({
            url: `../login/login?redirect=${currentPage}`
        });
    },
    goToPersonalHomePage() {
        const userInfo = wx.getStorageSync('userInfo');
        if (userInfo) {
            wx.navigateTo({
                url: `../personalHome/personalHome?userId=${userInfo.id}`,
            });
        }
    },
    goToMyCollectionPage() {
        wx.navigateTo({
            url: '../myCollection/myCollection',
        });
    },
    goToMyBrowsePage() {
        wx.navigateTo({
            url: '../myBrowse/myBrowse',
        });
    },
    goToMyPostPage() {
        wx.navigateTo({
            url: '../myPost/myPost',
        });
    },
    goToMyProductPage() {
        wx.navigateTo({
            url: '../myProduct/myProduct',
        });
    },
    goToMyOrderPage(e) {
        const {
            isbuy
        } = e.currentTarget.dataset;
        wx.navigateTo({
            url: `../myOrder/myOrder?isBuy=${isbuy}`,
        });
    }
})