// index.js
import productApi from '../../api/product/product'
import Message from 'tdesign-miniprogram/message/index';
const app = getApp(); // 获取全局对象
Page({
    data: {
        products: [], // 商品列表数据
        pageNo: 1, // 当前页码
        pageSize: 10, // 每页数量
        tabList: ['猜你喜欢', '最新发布'], // Tabs选项卡名称
        tab: 0, // 当前激活的tab选项
        hasMore: true, // 是否还有更多数据
        loading: false,
    },
    onLoad() {
        // 初始化加载数据
        this.refreshProducts(true);
    },
    onShow() {
        this.updateTabBar();
        // this.refreshProducts(true);
    },
    showNewMessage(obj) {
        Message.info(obj);
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
    // 切换tab
    changeTab: function (e) {
        const index = e.currentTarget.dataset.index;
        if (index !== this.data.tab) {
            this.setData({
                tab: index,
                pageNo: 1,
                products: [],
                hasMore: true
            }, () => {
                // 在数据更新完成后重新加载商品
                this.loadProducts();
            });
        }
    },
    refreshProducts: function (init = false) {
        // 如果是初始化加载，重置页面数据
        if (init) {
            this.setData({
                products: [],
                pageNo: 1,
                hasMore: true
            });
        }
        this.loadProducts();
    },
    // 加载商品
    loadProducts: function () {
        if (this.data.loading || !this.data.hasMore) {
            return;
        }
        this.setData({
            loading: true
        });

        // 请求获取商品数据
        productApi.list({
            pageNo: this.data.pageNo,
            pageSize: this.data.pageSize,
            tabValue: this.data.tab
        }).then(res => {
            const moreProducts = res.data.records || [];
            this.setData({
                products: this.data.products.concat(moreProducts), // 合并数据
                pageNo: this.data.pageNo + 1,
                loading: false,
                hasMore: this.data.pageNo < res.data.pages // 更新是否有更多数据
            });
        }).catch(err => {
            console.error('加载商品失败:', err);
            this.setData({
                loading: false
            });
        }).finally(() => {
            wx.stopPullDownRefresh(); // 停止下拉动作
        });
    },
    // 下拉刷新
    onPullDownRefresh: function () {
        this.refreshProducts(true);
    },
    // 页面触底时的处理
    onReachBottom: function () {
        this.refreshProducts(); // 触底时加载更多商品
    },
    // 跳转到搜索页面方法示例
    goToSearchPage: function () {
        wx.navigateTo({
            url: '../../pages/search/search',
        })
    },
    // 跳转到分类页面方法示例
    goToCategoryPage: function () {
        wx.navigateTo({
            url: '../../pages/category/category',
        })
    },
    // 跳转到商品详情页面方法示例
    goToProductDetail: function (e) {
        const productId = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: `../product-detail/product-detail?id=${productId}`,
        })
    }
})