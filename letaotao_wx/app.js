// app.js
App({
    onLaunch() {
        if (wx.getStorageSync('userId')) {
            this.connectWebSocket(wx.getStorageSync('userId'));
        }

    },
    // 定义连接 WebSocket 的方法
    connectWebSocket() {
        let {
            socketTask,
            baseSocketUrl
        } = this.globalData;
        const userId = wx.getStorageSync('userId');
        socketTask = wx.connectSocket({
            url: `${baseSocketUrl}/ws/notification?userId=${userId}`,
            header: {
                token: wx.getStorageSync('token')
            },
        });

        // 监听 WebSocket 连接打开事件
        socketTask.onOpen(() => {
            console.log('WebSocket 连接已打开');
        });

        // 监听 WebSocket 连接关闭事件
        socketTask.onClose(event => {
            if (event.code !== 1000) { // 如果code不是1000，表示连接非正常关闭
                console.log('WebSocket连接关闭，将重新连接');
                console.log(event.code,event.reason);
                this.connectWebSocket();
            }
        });

        // 监听 WebSocket 错误事件
        socketTask.onError((error) => {
            console.error('WebSocket 出现错误:', error);
        });

        // 监听 WebSocket 接收到服务器的消息事件
        socketTask.onMessage((res) => {
            const {
                message
            } = JSON.parse(res.data);
            console.log('新消息：', message);
            const pages = getCurrentPages();
            const page = pages[pages.length - 1];
  
            if (!page.route.includes('chat') && typeof page.showNewMessage === 'function') {
                this.globalData.needRefresh = true;
                let obj = {
                    context: page,
                    offset: ['180rpx', 32],
                    content: `${message.content}`,
                    icon: {
                        name: `${message.avatar}`
                    },
                    duration: -1,
                    link: {
                        content: '查看',
                        navigatorProps: {
                            url: '../message/message',
                            openType: 'switchTab'
                        },
                    },
                    closeBtn: true,
                }
                page.showNewMessage(obj);
            }
        });

    },
    globalData: {
        userInfo: null,
        activeTab: 0,
        hasLoadedComments: false,
        needRefresh: false,
        baseUrl: 'http://192.168.6.213:9998',
        baseSocketUrl: 'ws://192.168.6.213:9998',
        socketTask: null
    },

    setAddress: function(address) {
        // 存储选中的地址
        this.globalData.selectedAddress = address;
    },
    getSelectedAddress: function() {
        return this.globalData.selectedAddress;
    }
})