// pages/settings/settings.js
import userApi from '../../api/user/user'
import toast from '../../utils/toast'
import {
    clearUserInfo,
    setStorageProperty
} from '../../utils/storage'
import Message from 'tdesign-miniprogram/message/index';
const app = getApp();
Page({
    data: {
        userId: 0,
        popupVisible: false,
        currentSetting: {},
        currentValue: '',
        genders: [{
                label: '男',
                value: 0
            },
            {
                label: '女',
                value: 1
            }
        ],
        genderVisible: false,
        genderText: '',
        dateText: '',
        genderValue: [],
        dateVisible: false,
        date: new Date().getTime(),
        start: '1990-01-01 00:00:00',
        dateText: '',
    },
    onLoad(options) {
        let {
            userInfo
        } = options;
        if (!this.data.user) {
            if (userInfo !== 'undefined' && userInfo !== undefined) {
                userInfo = JSON.parse(decodeURIComponent(userInfo));
                this.setData({
                    user: userInfo,
                    userId: userInfo.id
                });
            } else {
                this.setData({
                    user: wx.getStorageSync('userInfo'),
                    userId: wx.getStorageSync('userId')
                });
            }
            this.setData({
                genderText: this.data.user.gender == 0 ? '男' : '女',
                genderValue: this.data.user.gender || '',
                dateText: this.data.user.birthday || '',
                date: new Date(this.data.user.birthday).getTime()
            });
        }
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    onNicknameClick: function () {
        this.setData({
            popupVisible: true,
            currentSetting: {
                type: 'nickName',
                title: '设置昵称'
            },
            currentValue: this.data.user.nickName
        });
    },
    onIntroClick: function () {
        this.setData({
            popupVisible: true,
            currentSetting: {
                type: 'introduction',
                title: '设置简介'
            },
            currentValue: this.data.user.introduction || ''
        });
    },
    onPopupVisibleChange: function (e) {
        this.setData({
            popupVisible: e.detail.visible
        });
    },
    hidePopup: function () {
        this.setData({
            popupVisible: false
        });
    },
    onConfirmPopup: function () {
        this.updateUserInfo();
        // 关闭popup
        this.setData({
            popupVisible: false
        });
    },
    onPopupInputChange: function (e) {
        this.setData({
            currentValue: e.detail.value
        });
    },
    async updateUserInfo() {
        if (!this.data.user) {
            toast.show('未登录，取消更改！');
            return;
        }
        let settingType = this.data.currentSetting.type;
        let newValue = this.data.currentValue;
        this.setData({
            [`user.${settingType}`]: newValue
        });
        console.log(settingType, newValue);
        // 构造参数请求更新用户信息
        let param = {
            userId: this.data.userId,
            [`${settingType}`]: newValue
        }
   
        let res = await userApi.updateInfo(param);
        if (res.success) {
            setStorageProperty('userInfo', settingType, newValue);
            toast.show(res.message);
        }
    },
    onGenderPickerChange(e) {
        const {
            value,
            label
        } = e.detail;
        this.setData({
            [`GenderVisible`]: false,
            [`genderValue`]: value.join(''),
            [`genderText`]: label.join(''),
            currentSetting: {
                type: 'gender'
            },
            currentValue: value.join('')
        });
        this.updateUserInfo();
    },
    onGenderPickerCancel() {
        this.setData({
            [`genderVisible`]: false,
        });
    },
    onGenderPicker() {
        this.setData({
            genderVisible: true
        });
    },
    showDatePicker() {
        this.setData({
            [`dateVisible`]: true,
        });
    },
    hideDatePicker() {
        this.setData({
            [`dateVisible`]: false,
        });
    },
    onDateConfirm(e) {
        const {
            value
        } = e.detail;
        this.setData({
            [`dateText`]: value,
            currentSetting: {
                type: 'birthday'
            },
            currentValue: value
        });
        this.updateUserInfo();
        this.hideDatePicker();
    },
    onAddressClick: function () {
        wx.navigateTo({
            url: '../../pages/address/address',
        });
    },
    onLogout: async function () {
        if (!this.data.user) {
            toast.show('未登录！');
            return;
        }
        let res = await userApi.logout();
        if (res.success) {
            clearUserInfo(); // 清除用户信息
            toast.show('退出成功').then(() => {
                setTimeout(() => {
                    wx.reLaunch({
                        url: '../../pages/profile/profile'
                    });
                }, 500);
            }).catch(() => {});
        }
    },
    onChooseAvatar(e) {
        const {avatarUrl} = e.detail;
        this.uploadAndUpdateUserAvatar(avatarUrl);
    },
    changeAvatar() {
        // 选择图片并上传
        wx.chooseMedia({
            count: 1,
            mediaType: 'image',
            success: (res) => {
                // 选取图片后
                const tempFilePath = res.tempFilePath;
                this.uploadAndUpdateUserAvatar(tempFilePath);
            }
        });
    },
    uploadAndUpdateUserAvatar(avatarUrl) {
        wx.uploadFile({
            url: app.globalData.baseUrl + '/api/lt_images/upload',
            filePath: avatarUrl,
            name: 'file',
            header: {
                token: wx.getStorageSync('token')
            },
            formData: {
                'module': 'letao-user-images',
                'relatedId': this.data.userId
            },
            success: (res) => {
                const data = JSON.parse(res.data);
                if (data.success) {
                    const image = data.data;
                    this.setData({
                        ['currentSetting.type']: 'avatar',
                        currentValue: image.url
                    }, () => {
                        this.updateUserInfo();
                    });
                } else {
                    console.error(data);
                }
            },
            fail(err) {
                console.error(err);
            }
        });
    }
})