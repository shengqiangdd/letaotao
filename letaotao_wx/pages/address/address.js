// pages/address/address.js
import addressApi from '../../api/address/address'
import toast from '../../utils/toast';
import Message from 'tdesign-miniprogram/message/index';
const app = getApp();
Page({
    data: {
        addressList: [],
        isManaging: false,
        showDeleteDialog: false,
        currentDeletingId: null, // 当前操作删除的地址ID
        needRefresh: false // 默认为 false，表示不需要重新获取数据
    },
    onLoad(options) {
        this.getAddressList();
        if (options.from === 'confirmPurchase') {
            this.setData({
                isSelectingAddress: true,
            });
        }
    },
    onShow() {
        if (app.globalData.needRefresh) {
            this.getAddressList();
            app.globalData.needRefresh = false; // 重置标志位
        }
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    getAddressList() {
        let userId = wx.getStorageSync('userId');
        addressApi.list({userId}).then(res => {
            if (res.success) {
                this.setData({
                    addressList: res.data
                });
            }
        }).catch(() => {});
    },
    formatRegion(region) {
        console.log(region);
        return region ? region.split('-').join(' ') : '';
    },
    selectAddress: function (e) {
        if (this.data.isSelectingAddress) {
            var selectedAddress = e.currentTarget.dataset.address;
            // 调用全局方法存储地址
            getApp().setAddress(selectedAddress);
            // 返回到上一个页面
            wx.navigateBack({
                delta: 1 // 返回的页面数，如果是1将返回上一个页面
            });
        }
    },
    // 跳转到编辑地址页面
    goToEditAddress: function (e) {
        const address = JSON.stringify(e.currentTarget.dataset.address);
        wx.navigateTo({
            url: `../../pages/editAddress/editAddress?address=${encodeURIComponent(address)}`
        });
    },
    // 添加地址
    addAddress: function () {
        wx.navigateTo({
            url: '../../pages/editAddress/editAddress',
        });
    },
    // 进入管理模式
    manageAddress: function () {
        this.setData({
            isManaging: true,
        });
    },
    // 设置默认地址
    setDefaultAddress: async function (e) {
        // 获取当前地址
        const item = e.currentTarget.dataset.item;
        console.log(item);
        // 判断如果当前已经是选中状态（即值为1），则反选（设置为0），否则设置为1
        const newStatus = item.isDefault === 1 ? 0 : 1;
        item.isDefault = newStatus;
        if(newStatus === 1) {
            let d = this.data.addressList.filter(a => a.isDefault === 1 && a.id !== item.id);
            if(d.length > 0) {
                d.forEach(async d => {
                    d.isDefault = 0;
                    await addressApi.update(d);
                });
            }
        }
        // 请求后端修改地址状态
        let res = await addressApi.update(item);
        if (res.success) {
            let addresses = this.data.addressList;
            let address = addresses.filter(a => a.id === item.id)[0];
            address.isDefault = item.isDefault;
            this.setData({
                addressList: addresses
            });
        }
    },
    // 切换管理模式
    toggleManage: function () {
        this.setData({
            isManaging: !this.data.isManaging,
        });
    },
    // 确认删除地址
    confirmDeleteAddress: function (e) {
        const id = e.currentTarget.dataset.id; // 获取需要删除的地址ID
        this.setData({
            showDeleteDialog: true,
            currentDeletingId: id,
        });
    },
    // 删除地址
    deleteAddress: async function () {
        const id = this.data.currentDeletingId;
        // TODO: 调用API从服务器删除地址
        let res = await addressApi.delete(id);
        if (res.success) {
            toast.show(res.message);
        }
        this.setData({
            showDeleteDialog: false,
        });
    },
    // 取消删除
    cancelDelete: function () {
        this.setData({
            showDeleteDialog: false,
        });
    }
})