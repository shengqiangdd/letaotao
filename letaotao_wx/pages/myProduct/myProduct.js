// pages/myProduct/myProduct.js
import productApi from '../../api/product/product'
import toast from '../../utils/toast'
import Message from 'tdesign-miniprogram/message/index';
Page({
    data: {
        products: [],
        currentStatus: 1, // 默认为'在卖'
        pageNo: 1, // 当前页码
        pageSize: 10, // 每页数量
        hasMore: true, // 是否还有更多数据
        loading: false,
    },
    onLoad() {
        // 初始化加载数据
        this.refreshProducts(true);
    },
    onTabChange: function (event) {
        const status = event.currentTarget.dataset.status;
        if (status !== this.data.currentStatus) {
            this.setData({
                currentStatus: status,
            });
            this.refreshProducts(true);
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
    showNewMessage(obj) {
        Message.info(obj);
    },
    loadProducts: function (flag) {
        if (this.data.loading || !this.data.hasMore) {
            return;
        }

        let params = {
            status: this.data.currentStatus,
            publisherId: wx.getStorageSync('userId'),
            pageNo: this.data.pageNo,
            pageSize: this.data.pageSize
        }
        this.setData({
            loading: true
        });
        if (flag) {
            this.setData({
                products: []
            });
        }

        productApi.listByMe(params).then(res => {
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
        this.loadProducts(true);
    },
    // 页面触底时的处理
    onReachBottom: function () {
        this.loadProducts(); // 触底时加载更多商品
    },
    goToProductDetail(e) {
        const {id} = e.currentTarget.dataset;
        wx.navigateTo({
          url: `../product-detail/product-detail?id=${id}`,
        });
    },
    onAskWithdraw: function (event) {
        const {
            item
        } = event.currentTarget.dataset;
        wx.showModal({
            title: '确认操作',
            content: '您确定要下架这个商品吗？',
            success: (res) => {
                if (res.confirm) {
                    this.withdrawProduct(item);
                }
            },
        });
    },
    withdrawProduct: async function (item) {
        let res = await productApi.withdraw(item);
        if (res.success) {
            toast.show(res.message);
            this.refreshProducts(true);
        }
    },
    editProduct: function (event) {
        const {
            item
        } = event.currentTarget.dataset;
        wx.navigateTo({
            url: `../publish/publish?id=${item.id}`,
        });
    },
    onAskDelete: function (event) {
        const {
            item
        } = event.currentTarget.dataset;
        wx.showModal({
            title: '确认删除',
            content: '您确定要删除这个商品吗？',
            success: (res) => {
                if (res.confirm) {
                    this.deleteProduct(item.id);
                }
            },
        });
    },
    deleteProduct: async function (id) {
        let res = await productApi.delete(id);
        if (res.success) {
            toast.show(res.message);
            this.refreshProducts(true);
        }
    },
    onAskRelist: function (event) {
        const {
            item
        } = event.currentTarget.dataset;
        wx.showModal({
            title: '确认操作',
            content: '您确定要重新上架这个商品吗？',
            success: (res) => {
                if (res.confirm) {
                    this.relistProduct(item);
                }
            },
        });
    },
    relistProduct: async function (item) {
        let res = await productApi.relist(item);
        if (res.success) {
            toast.show(res.message);
            this.refreshProducts(true);
        }
    },
})