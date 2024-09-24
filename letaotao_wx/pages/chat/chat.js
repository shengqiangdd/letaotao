// pages/chat/chat.js
import chatApi from '../../api/chat/chat'
import orderApi from '../../api/order/order';
import toast from '../../utils/toast';
const app = getApp();
Page({
    data: {
        relationId: 0,
        messageInput: '',
        seller: {},
        buyer: {},
        product: {},
        order: null,
        messages: [],
        userInfo: {},
        otherName: '',
        otherAvatar: '',
        showDeliveryConfirm: false,
        showReceiveConfirm: false,
        socketTask: null,
        socketOpen: false,
        socketUrl: app.globalData.baseSocketUrl + '/ws/chat',
        toView: '',
        viewerVisible: false,
        viewerImages: [],
    },
    onLoad: function (options) {
        let {
            info
        } = options;
        if (info !== undefined && info !== 'undefined') {
            const {
                relationId,
                seller,
                buyer,
                product
            } = JSON.parse(decodeURIComponent(info));

            const userId = wx.getStorageSync('userId');
            this.setData({
                relationId,
                seller,
                buyer,
                product,
                userId
            }, () => {
                this.connectWebSocket();
                this.determineOpponent();
                // 页面加载时获取聊天记录信息
                this.loadMessages(relationId);
                // 获取对应的订单信息
                this.getOrderByRelation();
            });
        }
    },
    determineOpponent() {
        // 确定对方是谁
        if (this.data.userId === this.data.seller.id) {
            // 如果当前用户是卖家，对方就是买家
            this.setData({
                otherId: this.data.buyer.id,
                otherAvatar: this.data.buyer.avatar,
                otherName: this.data.buyer.nickName,
            });
        } else {
            // 否则对方是卖家
            this.setData({
                otherId: this.data.seller.id,
                otherAvatar: this.data.seller.avatar,
                otherName: this.data.seller.nickName,
            });
        }
        this.setData({
            userInfo: wx.getStorageSync('userInfo')
        });
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
                    }, 100)
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

        // 监听WebSocket错误。
        this.data.socketTask.onError(error => {
            console.error('WebSocket错误', error);
        });

        this.data.socketTask.onClose(event => {
            if (event.code !== 1000) { // 如果code不是1000，表示连接非正常关闭
                console.log('WebSocket连接关闭，将重新连接');
                console.log(event.code,event.reason);
                let {socketUrl,relationId} = this.data;
                this._createNewSocketConnection(socketUrl, relationId);
            }
        });

        // 接收消息
        this.data.socketTask.onMessage(message => {
            const data = JSON.parse(message.data);
            console.log('收到服务器内容：', data);
            if (data.messageType === 'chat') {
                const {
                    messages
                } = this.data;
                messages.push(data.message);
                this.setData({
                    messages
                });
            } else if (data.messageType === 'order') {
                const {
                    order,
                    messages
                } = this.data;
                let newOrder;
                if(order) {
                    newOrder = order;
                    if(data.newStatus) {
                        newOrder.status = data.newStatus;
                    }
                    if(data.order.isEvaluate) {
                        newOrder.isEvaluate = data.order.isEvaluate;
                    }
                } else {
                    newOrder = data.order;
                }
                messages.push(data.message);
                this.setData({
                    order: newOrder,
                    messages
                });
            } else if (data.messageType === 'read') {
                const {
                    messages
                } = this.data;
                const readIds = data.readIds;
                const readIdsSet = new Set(readIds);
    
                const readMessages = messages.filter(message => readIdsSet.has(message.id));
                readMessages.forEach(r => r.isRead = 1);
                this.setData({
                    messages
                });
            }
            if (data.message && data.message.isRead === 0) {
                let userId = this.data.userInfo.id;
                if (userId === data.message.receiverId) {
                    let param = {
                        noReadIds: [data.message.id],
                        messageType: 'read'
                    }
                    this.sendSocketMessage(param);
                }
            }
            this.scrollToBottom();
        });
    },
    getOrderByRelation() {
        let param = {
            buyerId: this.data.buyer.id,
            sellerId: this.data.seller.id,
            productId: this.data.product.id,
            currentUserId: this.data.userInfo.id
        }
        orderApi.get(param).then(res => {
            if (res.success) {
                this.setData({
                    order: res.data
                });
            }
        }).catch(() => {});
    },
    loadMessages(relationId) {
        chatApi.messages(relationId).then(res => {
            if (res.success) {
                let userId = this.data.userInfo.id;
                let noReads = res.data.filter(m => userId === m.receiverId && m.isRead === 0);
    
                if (noReads.length > 0) {
                    let noReadIds = noReads.map(n => {
                        return n.id
                    });
                    let params = {
                        noReadIds: noReadIds,
                        messageType: 'read'
                    }
                    this.sendSocketMessage(params);
                }
                this.setData({
                    messages: res.data
                }, () => {
                    this.scrollToBottom();
                });
            }
        }).catch(() => {});
    },
    onBuy: async function () {
        if (this.data.order != null) {
            toast.show('已有订单，请去支付').then(() => {
                wx.navigateTo({
                    url: `../pay/pay?order=${decodeURIComponent(JSON.stringify(this.data.order))}`,
                });
            })
            return;
        }
        let res = await orderApi.checkAvailable(this.data.product.id);
        if (!res.success) {
            return toast.show(res.message);
        }
        const info = {
            product: this.data.product,
            sellerId: this.data.seller.id,
            buyerId: this.data.buyer.id
        }
        // 立即购买按钮的点击事件处理
        wx.navigateTo({
            url: `../confirmPurchase/confirmPurchase?info=${encodeURIComponent(JSON.stringify(info))}`
        });
    },
    onInput: function (e) {
        this.setData({
            messageInput: e.detail.value
        });
    },
    sendMessage: async function (message, isImage) {
        const {
            messageInput,
            userId,
            otherId,
            relationId
        } = this.data;

        const params = {
            content: (message && typeof message === 'string') ? message : messageInput,
            messageType: 'chat',
            senderId: userId,
            receiverId: otherId,
            relationId: relationId
        }
        if (isImage) {
            params['isImage'] = 1;
        }

        this.sendSocketMessage(params, () => {
            this.setData({
                messageInput: ''
            });
        });
    },
    sendSocketMessage(message, callback) {
        if (this.data.socketTask && this.data.socketOpen) {
            // 发送文本消息
            this.data.socketTask.send({
                data: JSON.stringify(message),
                success: () => {
                    console.log('消息发送成功');
                    if (callback) {
                        callback();
                    }
                },
                fail: (err) => console.error('消息发送失败:' + err)
            });
        } else {
            console.error('WebSocket连接未开启');
        }
    },
    onChooseImage: function () {
        // 选择图片并上传
        wx.chooseMedia({
            count: 1,
            mediaType: 'image',
            success: (res) => {
                // 选取图片后
                const tempFilePath = res.tempFiles[0].tempFilePath;
                console.log(tempFilePath);
                wx.uploadFile({
                    url: app.globalData.baseUrl + '/api/lt_images/upload',
                    filePath: tempFilePath,
                    name: 'file',
                    header: {
                        token: wx.getStorageSync('token')
                    },
                    formData: {
                        'module': 'letao-images',
                        'type': 'chat',
                        'relatedId': this.data.relationId
                    },
                    success: (res) => {
                        const data = JSON.parse(res.data);
                        // 上传成功后发送图片消息
                        if (data.success) {
                            const image = data.data;
                            this.sendMessage(image.url, true);
                        } else {
                            console.error(data);
                        }
                    },
                    fail(err) {
                        console.error(err);
                    }
                });
            }
        });
    },
    updateOrderStatus(status) {
        const {
            order,
            userId,
            otherId,
            relationId
        } = this.data;

        if (order) {
            const params = {
                newStatus: status,
                messageType: 'order',
                orderId: order.id,
                senderId: userId,
                receiverId: otherId,
                relationId: relationId
            }

            this.sendSocketMessage(params);
        }
    },
    showDeliveryDialog: function () {
        this.setData({
            showDeliveryConfirm: true
        });
    },
    hideDeliveryDialog: function () {
        this.setData({
            showDeliveryConfirm: false
        });
    },
    handleDeliveryConfirm: async function () {
        // 发起确认发货的网络请求
        let res = await orderApi.deliver(this.data.order);
        if (res.success) {
            toast.success(res.message);
            this.hideDeliveryDialog();
            // 更新订单状态
            this.updateOrderStatus(res.data.status);
        }
    },
    showReceiveDialog: function () {
        this.setData({
            showReceiveConfirm: true
        });
    },
    hideReceiveDialog: function () {
        this.setData({
            showReceiveConfirm: false
        });
    },
    handleReceiveConfirm: async function () {
        // 发起确认收货的网络请求
        let res = await orderApi.receive(this.data.order);
        if (res.success) {
            toast.success(res.message);
            this.hideReceiveDialog();
            let {product} = this.data;
            product.status = 4;
            this.setData({product});
            // 更新订单状态
            this.updateOrderStatus(res.data.status);
        }
    },
    onImageClick(e) {
        const {image} = e.currentTarget.dataset;
        this.setData({
            viewerImages: [image],
            viewerVisible: true
        });
    },
    onViewerClose(e) {
        const {
            trigger
        } = e.detail;
        this.setData({
            viewerVisible: false,
        });
    },
    goToEvaluate: function () {
        wx.navigateTo({
            url: '../evaluate/evaluate?orderId=' + this.data.order.id,
        });
    },
    goToProductDetail(e) {
        const {id} = e.currentTarget.dataset;
        wx.navigateTo({
          url: `../product-detail/product-detail?id=${id}`,
        });
    },
    goToUserPersonalHome(e) {
        const {id} = e.currentTarget.dataset;
        wx.navigateTo({
          url: `../personalHome/personalHome?userId=${id}`,
        });
    },
    scrollToBottom: function () {
        this.setData({
            toView: 'bottom'
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