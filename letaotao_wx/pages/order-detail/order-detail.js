// pages/order-detail/order-detail.js
import orderApi from '../../api/order/order';
Page({
    data: {
        order: null,
        userId: 0,
        steps: [{
                status: 1,
                content: '待付款'
            },
            {
                status: 2,
                content: '待发货'
            },
            {
                status: 3,
                content: '待收货'
            },
            {
                status: 4,
                content: '已完成'
            },
            {
                status: 5,
                content: '已取消'
            },
        ]
    },
    onLoad(options) {
        let {
            orderId
        } = options;
        this.setData({
            userId: wx.getStorageSync('userId')
        });
        if (orderId) {
            orderId = Number.parseInt(orderId);
            this.getOrder(orderId);
        }
    },
    getOrder(orderId) {
        orderApi.getOrderById(orderId).then(res => {
            if (res.success) {
                this.setData({
                    order: res.data
                });
            }
        });
    },
    goToProductDetail(e) {
        const {productid} = e.currentTarget.dataset;
        wx.navigateTo({
          url: `../product-detail/product-detail?id=${productid}`,
        });
    },
    contactSellerOrBuyer() {
        const {order} = this.data;
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
                wx.navigateBack({
                    delta: 1
                });
            }).catch(() => {});
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