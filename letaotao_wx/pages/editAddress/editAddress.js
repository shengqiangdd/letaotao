// pages/editAddress/editAddress.js
import addressApi from "../../api/address/address"
import {
    resetForm,
    validateField,
    isFormValid
} from '../../utils/form';
import toast from "../../utils/toast";
import Message from 'tdesign-miniprogram/message/index';
const app = getApp();
Page({
    data: {
        address: {
            contactPerson: '',
            phone: '',
            region: '请选择',
            detailAddress: '',
            isDefault: 0
        },
        isNew: true,
        // region: ['广东省', '广州市', '海珠区'],
        customItem: '全部',
        rules: {
            phone: {
                validate: value => /^1[3-9]\d{9}$/.test(value),
                message: "请输入有效的手机号码"
            },
            contactPerson: {
                validate: value => typeof value === 'string' && value.length >= 2,
                message: "联系人名称至少需要2个字符"
            },
            region: {
                validate: value => Array.isArray(value) && value.length >= 3,
                message: "请选择所在地区"
            },
            detailAddress: {
                validate: value => typeof value === 'string' && value.length >= 5,
                message: "详细地址至少需要5个字符"
            },
        }
    },
    onLoad(options) {
        let {
            address
        } = options;
        if (address !== undefined && address !== 'undefined') {
            address = JSON.parse(decodeURIComponent(address));
            address.region = address.region.split('-');
            this.setData({
                address,
                isNew: false
            });
        }
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    clearForm: function () {
        let address = resetForm(this.data.address);
        address.region = '请选择';
        this.setData({
            address
        });
    },
    // 删除地址函数
    deleteAddress: async function (e) {
        const {
            id
        } = e.currentTarget.dataset;
        wx.showModal({
            title: '确认删除',
            content: '您确定要删除这个地址吗？',
            success: async (res) => {
                if (res.confirm) {
                    let data = await addressApi.delete(id);
                    if (data.success) {
                        app.globalData.needRefresh = true;
                        toast.show(data.message).then(() => {
                            setTimeout(() => {
                                wx.navigateBack({
                                    delta: 1
                                });
                            }, 500);
                        });
                    }
                }
            },
        });
    },
    chooseAddress() {
        const that = this;
        wx.chooseAddress({
            success(res) {
                console.log(res);
                let region;
                if (res.streetName) {
                    region = [res.provinceName, res.cityName, res.countyName, res.streetName];
                } else {
                    region = [res.provinceName, res.cityName, res.countyName];
                }
                that.setData({
                    'address.contactPerson': res.userName,
                    'address.phone': res.telNumber,
                    'address.region': region,
                    'address.detailAddress': res.detailInfo
                });
            }
        });
    },
    // 当用户点击默认收货地址单选框时触发
    toggleDefaultAddress: function () {
        // 获取当前的选中状态
        const currentStatus = this.data.address.isDefault;
        // 判断如果当前已经是选中状态（即值为1），则反选（设置为0），否则设置为1
        const newStatus = currentStatus === 1 ? 0 : 1;

        this.setData({
            'address.isDefault': newStatus,
        });
    },
    onInputChange(e) {
        const {
            field
        } = e.currentTarget.dataset;
        const value = e.detail.value;
        this.setData({
            [`address.${field}`]: value
        }, () => {
            // 调用validateField进行即时验证
            validateField(field, value, this.data.rules);
        });
    },
    bindRegionChange: function (e) {
        const value = e.detail.value;
        // 更新地区选择的逻辑
        this.setData({
            'address.region': value
        });
    },
    saveAddress: async function () {
        const that = this;
        // 保存收货地址的逻辑
        if (!isFormValid(this.data.address, this.data.rules)) {
            return;
        }
        console.log(this.data.address);
        this.setData({
            'address.region': this.data.address.region.join('-')
        }, async () => {
            let res;
            let params = {
                userId: wx.getStorageSync('userId'),
                ...that.data.address
            }
            if (that.data.isNew) {
                res = await addressApi.add(params);
            } else {
                res = await addressApi.update(params);
            }
            if (res.success) {
                app.globalData.needRefresh = true;
                toast.show(res.message).then(() => {
                    setTimeout(() => {
                        wx.navigateBack({
                            delta: 1
                        });
                    }, 500);
                });
            }
        });
    }
})