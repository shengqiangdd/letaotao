// pages/confirmPurchase/confirmPurchase.js
import orderApi from "../../api/order/order";
import addressApi from "../../api/address/address";
import chatApi from '../../api/chat/chat'
import toast from "../../utils/toast";
const app = getApp();
Page({
    data: {
        address: {
            region: '选择地址',
            detailAddress: '',
            contactPerson: '',
            phone: '',
        },
        product: {},
        sellerId: 0,
        buyerId: 0,
        socketTask: null,
        socketOpen: false,
        socketUrl: app.globalData.baseSocketUrl + '/ws/chat',
        relationId: 0
    },
    onLoad(options) {
        const {
            info
        } = options;
        if (info !== undefined && info !== 'undefined') {
            const {
                sellerId,
                buyerId,
                product
            } = JSON.parse(decodeURIComponent(info));

            this.setData({
                product,
                buyerId,
                sellerId
            }, () => {
                this.getDefaultAddress();
            });
            this.getChatRelationId();
        }
    },
    onShow() {
        console.log('onShow');
        var selectedAddress = getApp().getSelectedAddress();
        if (selectedAddress) {
            // 更新页面数据
            this.setData({
                address: selectedAddress
            });
            console.log(selectedAddress);

            // 清空全局变量
            getApp().setAddress(null);
        }
    },
    getDefaultAddress() {
        const {
            address,
            buyerId
        } = this.data;
        if (!(address.id && address.id > 0)) {
            addressApi.getDefault(buyerId).then(res => {
                if (res.success) {
                    this.setData({
                        address: res.data
                    });
                }
            }).catch(() => {});
        }
    },
    async getChatRelationId() {
        const {
            buyerId,
            sellerId,
            product
        } = this.data;
        let params = {
            buyerId: buyerId,
            sellerId: sellerId,
            productId: product.id
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
    sendSocketMessage(order) {
        const {
            relationId
        } = this.data;

        const params = {
            newStatus: 1,
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
    onConfirmPurchase: async function () {
        const {
            address
        } = this.data;
        if (!(address.id && address.id > 0)) {
            return toast.show('请选择收货地址');
        }
        try {
            // 执行购买操作的逻辑
            let param = {
                sellerId: this.data.sellerId,
                buyerId: this.data.buyerId,
                productId: this.data.product.id,
                addressId: this.data.address.id,
                price: this.data.product.price
            }
            let res = await orderApi.create(param);
            toast.loading('创建订单中...').then(() => {
                if (res.success) {
                    const order = res.data;
                    toast.hide();
                    this.sendSocketMessage(order);
                    wx.navigateTo({
                        url: `../pay/pay?order=${encodeURIComponent(JSON.stringify(order))}`,
                    });
                } else {
                    toast.hide();
                    toast.show(res.message);
                }
            });
        } catch (error) {
            toast.hide();
            console.error(error);
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