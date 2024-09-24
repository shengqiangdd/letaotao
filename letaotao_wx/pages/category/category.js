// pages/category/category.js
import categoryApi from '../../api/category/category';
import Message from 'tdesign-miniprogram/message/index';
Page({
    data: {
        sideBarIndex: 1,
        scrollTop: 0,
        categories: [],
        currentCategory: [] // 用于存放当前选中分类的子分类
    },
    onLoad() {
        this.getCategoryList();
    },
    getCategoryList() {
        categoryApi.getCategoryTreeList().then(res => {
            if (res.success) {
                this.setData({
                    categories: res.data,
                    currentCategory: res.data[0].children
                })
            }
        }).catch(() => {})
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    onSideBarChange(e) {
        const {
            value
        } = e.detail;

        this.setData({
            sideBarIndex: value
        });
    },
    onSideBarItemClick(e) {
        const {
            category
        } = e.currentTarget.dataset; // 获取点击项的子分类数据
 
        this.setData({
            currentCategory: category || [], // 更新currentCategory
        });
    },
    goToShowList(e) {
        const {
            value,
            label
        } = e.currentTarget.dataset.item;

        wx.navigateTo({
            url: `../../pages/show-list/show-list?categoryId=${value}&label=${label}`,
        })
    }
})