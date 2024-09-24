// pages/personalHome/personalHome.js
import userApi from '../../api/user/user'
import productApi from '../../api/product/product'
import postApi from '../../api/post/post'
import likeApi from '../../api/like/like'
import Message from 'tdesign-miniprogram/message/index';
import evaluateApi from '../../api/evaluate/evaluate'

Page({
    data: {
        userId: 0,
        currentUserId: 0,
        userInfo: {},
        currentTab: 'goods', // 默认选项卡
        pageNo: 1, // 当前页码
        pageSize: 10,
        hasMore: true, // 是否还有更多数据
        loading: false,
        goodsList: [], // 商品列表数据
        postsList: [], // 帖子列表数据
        reviewsList: [], // 评价列表数据
    },
    onLoad(options) {
        const {
            userId
        } = options;
        this.setData({
            currentUserId: wx.getStorageSync('userId')
        });
        this.setData({
            userId: Number.parseInt(userId)
        }, () => {
            this.loadUserInfo(userId);
            this.loadDataForTab();
        });
    },
    loadUserInfo(userId) {
        userApi.get(userId).then(res => {
            if (res.success) {
                this.setData({
                    userInfo: res.data
                });
            }
        }).catch(() => {});
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    goToPersonalDetail() {
        wx.navigateTo({
            url: `../personalDetail/personalDetail?userInfo=${encodeURIComponent(JSON.stringify(this.data.userInfo))}`,
        });
    },
    goToEditProfile() {
        const {
            userInfo
        } = this.data;
        wx.navigateTo({
            url: `../settings/settings?userInfo=${decodeURIComponent(JSON.stringify(userInfo))}`,
        });
    },
    switchTab(event) {
        // 切换选项卡
        const tab = event.currentTarget.dataset.tab;
        this.setData({
            pageNo: 1,
            hasMore: true,
            currentTab: tab
        });
        // 加载新选项卡数据
        this.loadDataForTab(tab, true);
    },
    navigateToSearch() {
        wx.navigateTo({
            url: `../show-list/show-list?userId=${this.data.userInfo.id}`,
        });
    },
    goToFollowList(e) {
        const {
            type
        } = e.currentTarget.dataset;
        wx.navigateTo({
            url: `../follow-list/follow-list?followType=${type}&userId=${this.data.userInfo.id}`,
        });
    },
    navigateToCreate() {
        // 根据当前选项卡导航至发布界面
        const url = this.data.currentTab === 'goods' ? '../publish/publish' : '../post-publish/post-publish';
        wx.navigateTo({
            url
        });
    },
    navigateToGoodsDetail(event) {
        const {
            id
        } = event.currentTarget.dataset;
        // 导航至商品详情页
        wx.navigateTo({
            url: `../product-detail/product-detail?id=${id}`,
        });
    },
    navigateToPostDetail(event) {
        const {
            id
        } = event.currentTarget.dataset;
        // 导航至帖子详情页
        wx.navigateTo({
            url: `../post-detail/post-detail?id=${id}`,
        });
    },
    navigateToReviewDetail(e) {
        const {
            id
        } = e.currentTarget.dataset;
        const {userId,currentUserId} = this.data;
        let isMe = false;
        if(userId === currentUserId) {
            isMe = true;
        }
        wx.navigateTo({
            url: `../evaluate/evaluate?orderId=${id}&isMe=${isMe}`,
        });
    },
    async toggleLikePost(event) {
        const {
            id
        } = event.currentTarget.dataset;
        let posts = this.data.postsList;
        let post = posts.filter(p => p.id === id)[0];
        let param = {
            userId: this.data.userId,
            targetId: post.id,
            targetType: 1
        };
        let res = await likeApi.addOrUpdate(param);
        if (res.success) {
            post.isLiked = res.data;
            post.likeCount = post.isLiked ? post.likeCount + 1 : post.likeCount - 1;
            this.setData({
                postsList: posts
            });
        }
    },
    loadDataForTab(tab, flag) {
        const {
            currentTab,
            loading,
            hasMore,
            pageNo,
            pageSize,
            userId,
            currentUserId
        } = this.data;
        let _tab = tab ? tab : currentTab;
        if (loading || !hasMore) {
            return;
        }

        this.setData({
            loading: true
        });

        let res;
        let params = {
            pageNo,
            pageSize,
            userId,
            currentUserId
        }

        switch (_tab) {
            case 'goods':
                if (flag) {
                    this.setData({
                        goodsList: []
                    });
                }
                res = productApi.listByUserId(params);
                break;
            case 'posts':
                if (flag) {
                    this.setData({
                        postsList: []
                    });
                }
                res = postApi.listByUserId(params);
                break;
            case 'reviews':
                if (flag) {
                    this.setData({
                        reviewsList: []
                    });
                }
                res = evaluateApi.listByUserId(userId);
                break;
            default:
                break;
        }

        res.then(res => {
            const moreDatas = res.data.records || [];
            if (currentTab === 'goods') {
                this.setData({
                    goodsList: this.data.goodsList.concat(moreDatas)
                });
            } else if (currentTab === 'posts') {
                this.setData({
                    postsList: this.data.postsList.concat(moreDatas)
                });
            } else if (currentTab === 'reviews') {
                this.setData({
                    reviewsList: res.data
                });
            }
            if (currentTab !== 'reviews') {
                this.setData({
                    pageNo: this.data.pageNo + 1,
                    loading: false,
                    hasMore: this.data.pageNo < res.data.pages // 更新是否有更多数据
                });
            }

        }).catch(err => {
            console.error('加载数据失败:', err);
            this.setData({
                loading: false
            });
        }).finally(() => {
            wx.stopPullDownRefresh(); // 停止下拉动作
        });
    },
    onReachBottom: function () {
        this.loadDataForTab();
    }
})