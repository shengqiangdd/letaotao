// pages/myOrder/myOrder.js
import orderApi from "../../api/order/order";
import dayjs from 'dayjs';
import Message from 'tdesign-miniprogram/message/index';
import toast from "../../utils/toast";
Page({
    data: {
        orders: [],
        userId: 0,
        startDate: '',
        endDate: '',
        status: 0,
        currentStatusName: '全部商品',
        isBuy: true,
        popupVisible: false,
        pageNo: 1, // 当前页码
        pageSize: 10, // 每页数量
        hasMore: true, // 是否还有更多数据
        loading: false,
        searchValue: '',
        placeholder: '搜索买过的商品'
    },
    onLoad(options) {
        const {
            isBuy
        } = options;
        if (isBuy) {
            // 将字符串"true"或"false"转换为布尔值
            const isBuyBoolean = isBuy === 'true';
            this.setData({
                isBuy: isBuyBoolean,
                placeholder: !isBuyBoolean ? '搜索卖过的商品' : this.data.placeholder
            });
        }
        this.setData({
            userId: wx.getStorageSync('userId')
        }, () => {
            this.loadOrders();
        });

    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    loadOrders: function () {
        const {
            status,
            startDate,
            endDate,
            searchValue,
            isBuy,
            userId,
            pageNo,
            pageSize,
            loading,
            hasMore,
            orders
        } = this.data;

        if (loading || !hasMore) {
            return;
        }
        this.setData({
            loading: true
        });

        let params = {
            pageNo: pageNo,
            pageSize: pageSize,
            status,
            startDate,
            endDate,
            searchValue
        }

        if (isBuy) {
            params['buyerId'] = userId;
        } else {
            params['sellerId'] = userId;
        }
        orderApi.list(params).then(res => {
            if (res.success) {
                const moreOrders = res.data.records || [];

                this.setData({
                    orders: orders.concat(moreOrders), // 合并数据
                    pageNo: pageNo + 1,
                    loading: false,
                    hasMore: pageNo < res.data.pages // 更新是否有更多数据
                });
            }
        }).catch(err => {
            console.error('加载订单失败:', err);
            this.setData({
                loading: false
            });
        });
    },
    // 页面触底时的处理
    onReachBottom: function () {
        this.loadOrders(); // 触底时加载更多订单
    },
    search() {
        this.setData({
            orders: [],
            pageNo: 1,
            hasMore: true,
            loading: false
        });
        this.loadOrders();
    },
    showPopup: function () {
        this.setData({
            popupVisible: true
        });
    },
    selectTimeRange: function (e) {
        const {
            months
        } = e.currentTarget.dataset; // 获取按钮上的months数据
        const currentDate = dayjs(); // 获取当前日期

        // 计算开始日期（向前推几个月）
        const startDate = currentDate.subtract(months, 'month').format('YYYY-MM-DD');

        // 计算结束日期（当前日期）
        const endDate = currentDate.format('YYYY-MM-DD');

        this.setData({
            startDate,
            endDate,
        });
    },
    bindStartDateChange: function (e) {
        this.setData({
            startDate: e.detail.value
        });
    },
    bindEndDateChange: function (e) {
        this.setData({
            endDate: e.detail.value
        });
    },
    resetFilter: function () {
        // 重置筛选条件
        this.setData({
            startDate: '',
            endDate: ''
        });
    },
    applyFilter: function () {
        this.setData({
            orders: [],
            pageNo: 1,
            hasMore: true,
            loading: false
        });
        // 应用筛选条件并重新加载订单
        this.loadOrders();
        this.setData({
            popupVisible: false,
            startDate: '',
            endDate: ''
        });
    },
    onVisibleChange(e) {
        this.setData({
            popupVisible: e.detail.visible,
        });
    },
    changeTab: function (e) {
        const status = e.currentTarget.dataset.status;
        let statusName = '';
        switch (status) {
            case '0':
                statusName = '全部商品';
                break;
            case '1':
                statusName = '待付款';
                break;
            case '2':
                statusName = '待发货';
                break;
            case '3':
                statusName = '待收货';
                break;
            case '8':
                statusName = '待评价';
                break;
        }
        // 设置当前选中的标签和名称，并重新加载订单
        this.setData({
            status: status,
            currentStatusName: statusName,
            orders: [],
            pageNo: 1,
            hasMore: true
        }, () => {
            this.loadOrders();
        });
    },
    toPay(e) {
        const {
            item
        } = e.currentTarget.dataset;
        wx.navigateTo({
            url: `../pay/pay?order=${encodeURIComponent(JSON.stringify(item))}`,
        });
    },
    goToOrderDetail(e) {
        const {
            orderid
        } = e.currentTarget.dataset;
        wx.navigateTo({
            url: `../order-detail/order-detail?orderId=${orderid}`,
        });
    },
    contactSellerOrBuyer(e) {
        const {
            order
        } = e.currentTarget.dataset;
        let info = {
            relationId: order.relationId,
            seller: order.seller,
            buyer: order.buyer,
            product: order.product
        }
        wx.navigateTo({
            url: `../chat/chat?info=${encodeURIComponent(JSON.stringify(info))}`,
        });
    },
    showDeleteModal(e) {
        const {
            orderid
        } = e.currentTarget.dataset;
        wx.showModal({
            title: '确认删除',
            content: '您确定要删除这个订单吗？',
            success: res => {
                if (res.confirm) {
                    this.deleteOrder(orderid);
                }
            }
        });
    },
    async deleteOrder(orderid) {
        let res = await orderApi.delete(orderid);
        if (res.success) {
            toast.show(res.message).then(() => {
                this.setData({
                    orders: [],
                    pageNo: 1,
                    hasMore: true
                }, () => {
                    this.loadOrders();
                });
            }).catch(() => {});
        } else {
            toast.error(res.message);
        }
    },
    goToEvaluatePage(e) {
        const {
            orderid
        } = e.currentTarget.dataset;
        wx.navigateTo({
            url: `../evaluate/evaluate?orderId=${orderid}`,
        });
    }

})