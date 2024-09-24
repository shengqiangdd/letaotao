// pages/product-detail/product-detail.js
import productApi from '../../api/product/product';
import userApi from '../../api/user/user';
import commentApi from '../../api/comment/comment';
import collectionApi from '../../api/collection/collection';
import chatApi from '../../api/chat/chat'
import toast from '../../utils/toast'
import Message from 'tdesign-miniprogram/message/index';
Page({
    data: {
        product: {},
        userId: 0,
        userInfo: {},
        viewerVisible: false,
        showViewerIndex: true,
        viewerImages: [],
        viewerInitialIndex: 0,
        popupVisible: false, // 弹出框的可见性和输入框的焦点
        inputFocus: false,
        placeholderText: "", // 输入框的占位符
        newCommentContent: "", // 新的评论的内容
        editingCommentParentId: 0,
    },
    onLoad: function (options) {
        this.initData(options);
    },
    onShow() {
        const app = getApp();
        if (app.globalData.needRefresh) {
            this.initData(this.data.product);
        }
    },
    initData(options) {
        this.setData({
            userInfo: wx.getStorageSync('userInfo') || {},
            userId: wx.getStorageSync('userId') || 0
        });
        if (options.id) {
            const app = getApp()
            if(app.globalData.hasLoadedComments) {
                app.globalData.hasLoadedComments = false
            }
            // 加载商品数据
            this.getProduct(options.id);
        }
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    getProduct(id) {
        productApi.get(id).then(res => {
            if (res.success) {
                this.setData({
                    product: res.data
                }, () => {
                    let {
                        id,
                        images,
                        price
                    } = this.data.product;
                    this.recordBrowsingHistory(id, images[0], price);
                });
            }
        }).catch({});
    },
    followUser: async function () {
        const userId = this.data.userId;
        const product = this.data.product;
        if (userId == product.publisherId) {
            return toast.show('自己不能关注自己~');
        }
        let params = {
            followerId: userId,
            followedId: product.publisherId
        }
        let res = await userApi.follow(params);
        if (res.success) {
            product.isFollowed = res.data;
            this.setData({
                product
            });
        }
    },
    recordBrowsingHistory: function (id, img, price) {
        const today = this.formatDate(new Date());
        let browsingSession = wx.getStorageSync('browsingSession') || [];

        // 查找是否存在今天的记录
        let todayRecord = browsingSession.find(record => record.date === today);

        if (!todayRecord) {
            // 如果今天还没有记录，添加一个新的日期记录
            todayRecord = {
                date: today,
                items: []
            };
            browsingSession.push(todayRecord);
        }

        // 检查当前商品ID是否已存在今日的浏览记录中
        let existingRecord = todayRecord.items.find(item => item.id === id);
        if (!existingRecord) {
            // 如果没有该商品的记录，添加新的记录到今天的日期下
            todayRecord.items.push({
                id,
                img,
                price
            });
            wx.setStorageSync('browsingSession', browsingSession);
        }
    },
    formatDate: function (date) {
        // 格式化日期为字符串 'yyyy-mm-dd'
        let month = '' + (date.getMonth() + 1),
            day = '' + date.getDate(),
            year = date.getFullYear();

        if (month.length < 2)
            month = '0' + month;
        if (day.length < 2)
            day = '0' + day;

        return [year, month, day].join('-');
    },
    goToPersonalHomePage() {
        wx.navigateTo({
          url: `../personalHome/personalHome?userId=${this.data.product.publisherId}`,
        });
    },
    onCommentCountChange(e) {
        const {
            newCommentCount
        } = e.detail;

        let product = this.data.product;
        product.commentCount += newCommentCount;
        // 更新商品留言计数
        this.setData({
            product
        });
    },
    showAllCommentsPopup() {
        this.selectComponent('.all-comments').setData({
            allCommentsPopupVisible: true
        });
    },
    onInputVisibleChange(e) {
        this.setData({
            popupVisible: e.detail.visible
        });
    },
    replyComment: function (e) {
        const nickName = this.data.product.nickName;
        const parentId = 0;
        this.setData({
            popupVisible: true,
            placeholderText: "给" + nickName + "留言：",
            inputFocus: true,
            editingCommentParentId: parentId
        });
    },
    handleInput: function (e) {
        this.setData({
            newCommentContent: e.detail.value
        });
    },
    addComment: async function () {
        const parentId = this.data.editingCommentParentId || 0;
        const newComment = {
            content: this.data.newCommentContent,
            parentId: Number.parseInt(parentId),
            userId: this.data.userId,
            referenceId: this.data.product.id,
            type: 1
        };
        if(newComment.content === '' || newComment.content === null) {
            return;
        }

        let res = await commentApi.add(newComment);
        if (res.success) {
            this.selectComponent('.all-comments').setData({
                newComment: res.data
            });
            this.setData({
                popupVisible: false,
                newCommentContent: "",
                editingCommentParentId: 0
            });
        }
    },
    onImageClick(e) {
        const {
            images,
            index
        } = e.currentTarget.dataset;
        this.setData({
            viewerImages: images.map(i => i.url),
            viewerInitialIndex: index,
            showViewerIndex: true,
            viewerVisible: true
        });
    },
    onViewerClose(e) {
        this.setData({
            viewerVisible: false,
        });
    },
    toggleFavorite: async function () {
        // 收藏或取消收藏商品逻辑
        const product = this.data.product;
        let res = await collectionApi.addOrUpdate({
            userId: wx.getStorageSync('userId'),
            targetId: product.id,
            targetType: 1
        });
        if (res.success) {
            product.isFavorite = res.data;
            product.collectCount = product.isFavorite ? product.collectCount + 1 : product.collectCount - 1;
            this.setData({
                product
            });
        }
    },
    async contactSeller() {
        const seller = {
            id: this.data.product.publisherId,
            avatar: this.data.product.avatar,
            nickName: this.data.product.nickName
        }
        const buyer = {
            id: this.data.userId,
            avatar: this.data.userInfo.avatar,
            nickName: this.data.userInfo.nickName
        }
        const product = {
            id: this.data.product.id,
            publisherId: this.data.product.publisherId,
            name: this.data.product.name,
            image: this.data.product.images[0],
            price: this.data.product.price
        }
        let params = {
            buyerId: buyer.id,
            sellerId: seller.id,
            productId: product.id
        }
        let res = await chatApi.relation(params);
        if (res.success) {
            const info = {
                relationId: res.data.id,
                seller,
                buyer,
                product
            }
            wx.navigateTo({
                url: `../../pages/chat/chat?info=${encodeURIComponent(JSON.stringify(info))}`
            });
        }

    }
});