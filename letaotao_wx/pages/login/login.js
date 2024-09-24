// pages/login/login.js
import toast from '../../utils/toast';
import userApi from '../../api/user/user'
import {
    checkAndAuthorize
} from '../../utils/auth'
const defaultAvatarUrl = 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132'
const app = getApp();

Page({
    data: {
        userInfo: {},
        avatarUrl: defaultAvatarUrl,
        nickName: '', // 用于存储用户输入的昵称
        showCustomDialog: false, // 用于控制自定义对话框的显示
    },
    onLoad(options) {
        this.setData({
            redirectUrl: decodeURIComponent(options.redirect || '/')
        });
    },
    onLogin(e) {
        const that = this;
        checkAndAuthorize('scope.userInfo', () => {
            wx.getUserProfile({
                desc: '用于获取用户信息',
                success: function (res) {
                    let userInfo = res.userInfo;
                    that.doLogin(userInfo);
                }
            });
        });
    },
    doLogin(userInfo) {
        if (userInfo) {
            console.log('用户信息：', userInfo);
            // 调用微信登录接口
            wx.login({
                success: (res) => {
                    if (res.code) {
                        let params = {
                            code: res.code,
                            userInfo: userInfo
                        }
                        // 显示 loading 提示，告知用户正在登录
                        toast.loading('登录中...');
                        // 发起网络请求，将code发给服务器端处理
                        userApi.login(params).then(res => {
                            if (res.success) { // 登录成功，保存 token 到本地
                        
                                // 将 token 与 expireTime 存在本地缓存
                                wx.setStorageSync('token', res.data.token);
                                wx.setStorageSync('tokenExpire',res.data.expireTime);
                                // 缓存用户信息
                                wx.setStorageSync('userId', res.data.id);
                                const {
                                    userInfo
                                } = res.data;
                                this.setData({
                                    userInfo
                                });
                                // 微信小程序不再支持获取真实用户昵称和用户头像，需用户自行设置
                                if (userInfo.isNewUser) {
                                    this.setData({
                                        showCustomDialog: true,
                                    });
                                } else {
                                    wx.setStorageSync('userInfo', userInfo);
                                    this.goBack();
                                }

                            } else {
                                toast.hide();
                                toast.show("登录失败：" + res.message);
                            }
                        }).catch(err => {
                            console.log('登录异常：' + err);
                            toast.hide();
                        })
                    } else {
                        console.log('登录失败！' + res.errMsg);
                    }
                },
            })
        } else {
            // 用户拒绝授权，给出提示
            toast.show('需要授权才能登录');
        }
    },
    // 返回上一页面
    goBack() {
        app.globalData.needRefresh = true;
        // tabBar页面路径列表
        const tabBarPages = [
            'pages/index/index',
            'pages/community/community',
            'pages/message/message',
            'pages/profile/profile'
        ];

        let redirectUrl = this.data.redirectUrl;
        // 移除 redirectUrl 的开头斜杠 “/” 以确保格式一致
        redirectUrl = redirectUrl.startsWith('/') ? redirectUrl.slice(1) : redirectUrl;

        app.connectWebSocket();
        // 检查清理后的 redirectUrl 是否在 tabBarPages 列表中
        if (tabBarPages.includes(redirectUrl)) {
            // 如果是 tabBar 页面，使用 switchTab 方法跳转
            wx.switchTab({
                url: `/${redirectUrl}`,
                complete: () => {
                    // 无论成功或失败都会调用，隐藏 loading 提示
                    toast.hide();
                }
            });
        } else {
            // 如果不是 tabBar 页面，使用 navigateTo 方法跳转
            wx.navigateBack({
                delta: 1, // 返回前一页面
                complete: () => {
                    // 无论成功或失败都会调用，隐藏 loading 提示
                    toast.hide();
                }
            });
        }
    },
    // 当输入昵称时触发
    onNicknameInput: function (e) {
        console.log(e.detail);
        this.setData({
            nickName: e.detail.value
        });
    },
    // 当选择头像之后调用该方法
    onChooseAvatar: function (e) {
        const avatarUrl = e.detail.avatarUrl;
        this.setData({
            avatarUrl
        });
    },
    // 当用户点击确定按钮时调用该方法
    confirmCustomization: function (e) {
        const {
            avatarUrl,
            nickName
        } = this.data;

        if (nickName != null && nickName != undefined && avatarUrl != defaultAvatarUrl) {
            // 上传图片
            wx.uploadFile({
                url: app.globalData.baseUrl + '/api/lt_images/upload', // 图片上传接口URL
                filePath: avatarUrl, // 本地临时文件路径
                name: 'file', // 根据后端接受字段来设置
                header: {
                    token: wx.getStorageSync('token')
                },
                formData: {
                    'module': 'letao-user-images',
                    'relatedId': wx.getStorageSync('userId')
                },
                success: async (res) => {
                    const data = JSON.parse(res.data);
                    console.log(data);
                    if (data.success) {
                        // 请求后端更新用户信息
                        let params = {
                            userId: wx.getStorageSync('userId'),
                            avatarUrl: data.data,
                            nickName: nickName
                        };
                        let res = await userApi.updateInfo(params);
                        if (res.success) {
                            // 更新userInfo
                            this.setData({
                                'userInfo.avatar': data.data,
                                'userInfo.nickName': nickName
                            }, () => {
                                wx.setStorageSync('userInfo', this.data.userInfo);
                            });
                            // 关闭对话框
                            this.closeDialog();
                        }

                    } else {
                        toast.show('上传图片失败');
                    }
                },
                fail: (error) => log(error)
            });
        } else {
            toast.show('头像或者昵称不能为空！');
        }
    },
    // 关闭对话框
    closeDialog: function () {
        this.setData({
            showCustomDialog: false,
        });
        this.goBack();
    }
})