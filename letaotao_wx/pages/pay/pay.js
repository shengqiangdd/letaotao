// pages/pay/pay.js
import orderApi from "../../api/order/order";
import chatApi from '../../api/chat/chat'
import toast from "../../utils/toast";
let countdownInterval;
const app = getApp();
Page({
    data: {
        order: {}, // 从上一个页面接收订单
        remainingTime: '30:00', // 初始剩余时间，需要动态计算
        relationId: 0,
        socketTask: null,
        socketOpen: false,
        socketUrl: app.globalData.baseSocketUrl + '/ws/chat'
    },
    onLoad(options) {
        const order = JSON.parse(decodeURIComponent(options.order));
        if (!order.timeout) {
            this.getOrder(order);
        } else {
            this.setData({
                order
            }, () => {
                this.getChatRelationId();
                this.startCountdown(order.timeout);
            });

        }
    },
    // 当页面卸载时，清除计时器
    onUnload: function () {
        clearInterval(countdownInterval);
        this.closeWebSocket();
    },
    async getOrder(order) {
        let res = await orderApi.getOrderById(order.id);
        if (res.success) {
            console.log(res.data);
            this.setData({
                order: res.data
            }, () => {
                this.getChatRelationId();
                this.startCountdown(res.data.timeout);
            });
        }
    },
    async getChatRelationId() {
        const {
            order
        } = this.data;
        let params = {
            buyerId: order.buyerId,
            sellerId: order.sellerId,
            productId: order.productId
        }
        let res = await chatApi.relation(params);
        if (res.success) {
            this.setData({
                relationId: res.data.id
            }, () => {
                console.log(this.data.relationId);
                this.connectWebSocket();
            });
        }
    },
    // 连接WebSocket
    connectWebSocket: function () {
        const {
            socketUrl,
            relationId
        } = this.data;

        // 如果已有连接并且状态为已建立(1)，则无需重新连接
        if (this.data.socketTask && this.data.socketTask.readyState === 1) {
            return;
        }

        // 如果已有连接但状态非已建立，关闭旧连接并重新建立
        if (this.data.socketTask) {
            this.data.socketTask.close({
                success: () => {
                    setTimeout(() => {
                        this._createNewSocketConnection(socketUrl, relationId);
                    }, 100);
                },
                fail: () => console.error('关闭旧WebSocket连接失败'),
            });
        } else {
            this._createNewSocketConnection(socketUrl, relationId);
        }
    },
    _createNewSocketConnection(socketUrl, relationId) {
        this.data.socketTask = wx.connectSocket({
            url: `${socketUrl}?chatId=${relationId}`,
            header: {
                token: wx.getStorageSync('token')
            },
            success: () => console.log('WebSocket连接成功'),
            fail: () => console.error('WebSocket连接失败')
        });

        // 监听WebSocket连接打开
        this.data.socketTask.onOpen(() => {
            this.setData({
                socketOpen: true
            });
            console.log('WebSocket连接已打开！');
        });

        this.data.socketTask.onClose(event => {
            if (event.code !== 1000) { // 如果code不是1000，表示连接非正常关闭
                console.log('WebSocket连接关闭，将重新连接');
                console.log(event.code, event.reason);
                let {
                    socketUrl,
                    relationId
                } = this.data;
                this._createNewSocketConnection(socketUrl, relationId);
            }
        });

        // 监听WebSocket错误。
        this.data.socketTask.onError(error => {
            console.error('WebSocket错误', error);
        });
    },
    sendSocketMessage(status) {
        const {
            order,
            relationId
        } = this.data;

        const params = {
            newStatus: status,
            messageType: 'order',
            orderId: order.id,
            senderId: order.buyerId,
            receiverId: order.sellerId,
            relationId: relationId
        }
        if (this.data.socketTask && this.data.socketOpen) {
            // 发送文本消息
            this.data.socketTask.send({
                data: JSON.stringify(params),
                success: () => {
                    console.log('消息发送成功', params);
                },
                fail: (err) => console.error('消息发送失败:' + err)
            });
        } else {
            console.error('WebSocket连接未开启');
        }
    },
    startCountdown: function (timeout) {
        // 计算剩余的秒数
        const endTime = new Date(timeout).getTime();
        if (isNaN(endTime)) {
            this.setData({
                remainingTime: '时间已过期'
            }, () => {
                wx.navigateTo({
                    url: '../myOrder/myOrder',
                });
            });
        }
        countdownInterval = setInterval(() => {
            const now = new Date().getTime();
            const distance = endTime - now;

            if (distance < 0) {
                clearInterval(countdownInterval);
                this.setData({
                    remainingTime: '时间已过期'
                }, () => {
                    wx.redirectTo({
                        url: '../myOrder/myOrder',
                    });
                });
            } else {
                let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                let seconds = Math.floor((distance % (1000 * 60)) / 1000);
                // 更新剩余时间
                this.setData({
                    remainingTime: this.formatTime(minutes) + ':' + this.formatTime(seconds)
                });
            }
        }, 1000); // 每秒更新一次
    },
    formatTime: function (time) {
        return time < 10 ? '0' + time : time;
    },
    onPay: async function () {
        try {
            toast.loading('支付中...').then(async () => {
                let res = await orderApi.pay(this.data.order);

                if (res.success) {
                    toast.hide();
                    this.sendSocketMessage(2);
                    toast.success(res.message).then(() => {
                        // 处理成功支付后的逻辑，比如跳转到订单详情页
                        wx.redirectTo({
                            url: '../myOrder/myOrder',
                        });
                    });
                } else {
                    toast.hide();
                    toast.show(res.message);
                }
            });
        } catch (error) {
            toast.hide();
            console.error(error);
            toast.show('支付失败，请重试');
        }
    },
    onCancelOrder: async function () {
        try {
            toast.loading('取消中...').then(async () => {
                let res = await orderApi.cancel(this.data.order);
                if (res.success) {
                    toast.hide();
                    this.sendSocketMessage(5);
                    toast.success(res.message).then(() => {
                        wx.redirectTo({
                            url: '../myOrder/myOrder',
                        });
                    });
                } else {
                    toast.hide();
                    toast.show(res.message);
                }
            });
        } catch (error) {
            console.error(error);
            toast.show('取消订单失败，请重试');
        }
    },
    onHide: function () {
        this.closeWebSocket();
    },
    onShow: function () {
        if (!this.data.socketOpen && !this.data.socketTask && this.data.relationId !== 0) {
            // 如果连接未打开且socketTask未定义，则尝试重新连接
            this.connectWebSocket();
        }
    },
    closeWebSocket() {
        if (this.data.socketTask) {
            this.data.socketTask.close({
                success: () => {
                    console.log('WebSocket连接已关闭！');
                    this.setData({
                        socketTask: null,
                        socketOpen: false
                    }); // 关闭成功后设置socketTask为null，并关闭标记
                },
                fail: () => console.error('关闭WebSocket连接失败')
            });
        }
    },
    onUnload() {
        this.closeWebSocket();
    }
})