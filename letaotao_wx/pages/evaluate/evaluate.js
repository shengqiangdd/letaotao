// pages/evaluate/evaluate.js
import orderApi from '../../api/order/order'
import evaluateApi from '../../api/evaluate/evaluate'
import chatApi from '../../api/chat/chat'
const app = getApp();
Page({
    data: {
        order: null,
        evaluations: [], // 评价列表
        userId: 0,
        isMe: true,
        isEvaluate: false, // 是否已评价
        visible: false, // 控制t-popup显示隐藏
        content: '', // 用户输入的评价
        isFavor: 1, // 1 - 好评，0 - 差评，默认好评
        socketTask: null,
        socketOpen: false,
        socketUrl: app.globalData.baseSocketUrl + '/ws/chat',
        relationId: 0
    },
    onLoad(options) {
        let {
            orderId,
            isMe
        } = options;
        if (isMe) {
            isMe = isMe === 'true';
            this.setData({
                isMe
            });
        }

        this.setData({
            userId: wx.getStorageSync('userId')
        }, () => {
            if (orderId) {
                orderId = Number.parseInt(orderId);
                if (this.data.isMe) {
                    this.getEvaluateStatus(orderId);
                }
                this.getOrder(orderId);
            }
        });

    },
    getOrder(orderId) {
        orderApi.getOrderById(orderId).then(res => {
            if (res.success) {
                this.setData({
                    order: res.data
                }, () => {
                    this.getEvaluations();
                    if (this.data.isMe) {
                        this.getChatRelationId();
                    }
                });
            }
        }).catch(() => {});
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
                this.connectWebSocket();
            });
        }
    },
    getEvaluations() {
        let {
            order,
            userId
        } = this.data;
   
        evaluateApi.listByOrderId(order.id).then(res => {
            if (res.success) {
                let evaluations = res.data.map(e => {
                    return {
                        ...e,
                        isMe: e.userId === userId,
                        role: order.buyerId === e.userId ? '买家·' : (order.sellerId === e.userId ? '卖家·' : '')
                    }
                });
                this.setData({
                    evaluations
                });
            }
        });
    },
    // 连接WebSocket
    connectWebSocket: function () {
        if(this.data.isEvaluate) {
            return;
        }
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
            fail: (err) => console.error('WebSocket连接失败',err)
        });

        // 监听WebSocket连接打开
        this.data.socketTask.onOpen(() => {
            this.setData({
                socketOpen: true
            });
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
    sendSocketMessage() {
        const {
            relationId,
            order
        } = this.data;
        const userId = wx.getStorageSync('userId');
        const receiverId = userId === order.sellerId ? order.buyerId : order.sellerId;

        const params = {
            newStatus: 6,
            messageType: 'order',
            orderId: order.id,
            senderId: userId,
            receiverId: receiverId,
            relationId: relationId
        }
        if (this.data.socketTask && this.data.socketOpen) {
            // 发送文本消息
            this.data.socketTask.send({
                data: JSON.stringify(params),
                fail: (err) => console.error('消息发送失败:' + err)
            });
        } else {
            console.error('WebSocket连接未开启');
        }
    },
    getEvaluateStatus(orderId) {
        let params = {
            orderId: orderId,
            userId: this.data.userId
        }
        evaluateApi.isEvaluate(params).then(res => {
            if (res.success) {
                this.setData({
                    isEvaluate: res.data
                });
            }
        }).catch(() => {});
    },
    // 显示评价弹窗
    showPopup() {
        this.setData({
            visible: true
        });
    },
    // 关闭评价弹窗
    closePopup() {
        this.setData({
            visible: false
        });
    },
    // 输入评价内容时
    inputChange(e) {
        this.setData({
            content: e.detail.value
        });
    },
    // 选择评价类型时
    radioChange(e) {
        this.setData({
            isFavor: e.detail.value
        });
    },
    // 提交评价
    async submitEvaluation() {
        const {
            order,
            content,
            isFavor,
            evaluations
        } = this.data;
        const userId = wx.getStorageSync('userId');
        const params = {
            orderId: order.id,
            userId: userId,
            content: content,
            isFavor: isFavor
        }
        let res = await evaluateApi.evaluate(params);
        if (res.success) {
            let userInfo = wx.getStorageSync('userInfo');
            res.data.avatar = userInfo.avatar;
            res.data.nickName = userInfo.nickName;
            res.data.isMe = true;
            res.data.role = userId === order.buyerId ? '买家·' : '卖家·';
            evaluations.push(res.data);
            this.setData({
                visible: false,
                isEvaluate: true,
                evaluations
            }, () => {
                this.sendSocketMessage();
                // this.closePopup();
            });
        }
    },
    onVisibleChange(e) {
        this.setData({
            visible: e.detail
        });
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