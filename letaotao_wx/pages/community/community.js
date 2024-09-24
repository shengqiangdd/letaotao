// pages/community/community.js
import postApi from '../../api/post/post'
import likeApi from '../../api/like/like'
import userApi from '../../api/user/user'
import toast from '../../utils/toast';
import Message from 'tdesign-miniprogram/message/index';
const app = getApp(); // 获取全局对象
Page({
    data: {
        posts: [], // 初始帖子数据数组
        pageNo: 1, // 当前页码
        pageSize: 10,
        hasMore: true, // 是否还有更多数据
        loading: false,
        tab: 0,
        tabList: ['推荐', '关注'], // Tabs选项卡名称
        viewerVisible: false,
        showViewerIndex: true,
        viewerImages: [],
        viewerInitialIndex: 0,
        followerId: 0
    },
    onLoad: function () {
        // 初始加载帖子
        this.refreshPosts(true);
    },
    onShow() {
        this.updateTabBar();
        const app = getApp();
        if(app.globalData.needRefresh === true) {
            this.refreshPosts(true);
            app.globalData.needRefresh = false;
        }
    },
    updateTabBar() {
        const activeTab = app.globalData.activeTab; // 获取全局变量tab状态
        if (typeof this.getTabBar === 'function') {
            this.getTabBar((tabBar) => {
                tabBar.setData({
                    currentTab: activeTab
                });
            });
        }
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    refreshPosts: function (init = false) {
        // 如果是初始化加载，重置页面数据
        if (init) {
            this.setData({
                posts: [],
                pageNo: 1,
                hasMore: true
            });
        }
        this.loadPosts();
    },
    loadPosts: function () {
        if (this.data.loading || !this.data.hasMore) {
            return;
        }
        this.setData({
            loading: true
        });
        // 使用接口请求获取帖子数据
        postApi.list({
            pageNo: this.data.pageNo,
            pageSize: this.data.pageSize,
            currentUserId: wx.getStorageSync('userId'),
            followerId: this.data.followerId
        }).then(res => {
            const morePosts = res.data.records || [];
            this.setData({
                posts: this.data.posts.concat(morePosts), // 合并数据
                pageNo: this.data.pageNo + 1,
                loading: false,
                hasMore: this.data.pageNo < res.data.pages // 更新是否有更多数据
            });
        }).catch(err => {
            console.error('加载帖子失败:', err);
            this.setData({
                loading: false
            });
        }).finally(() => {
            wx.stopPullDownRefresh(); // 停止下拉动作
        });
    },
    // 下拉刷新
    onPullDownRefresh: function () {
        this.refreshPosts(true);
    },
    onReachBottom: function () {
        this.loadPosts(); // 触底时加载更多帖子
    },
    switchTab: function (e) {
        const index = e.currentTarget.dataset.index;
        if (index !== this.data.tab) {
            if(index == 1) {
                this.setData({followerId: wx.getStorageSync('userId')});
            } else {
                this.setData({followerId: 0});
            }
            // 切换到新的tab，重置相关数据
            this.setData({
                posts: [],
                pageNo: 1,
                hasMore: true,
                tab: index
            }, () => {
                // 在数据更新完成后重新加载帖子
                this.loadPosts();
            });
        }
    },
    goToPostDetail(e) {
        let id = e.currentTarget.dataset.id;
        wx.navigateTo({
          url: `../post-detail/post-detail?id=${id}`,
        });
    },
    onImageClick(e) {
        const {images,index} = e.currentTarget.dataset;
        this.setData({
            viewerImages: images.map(i => i.url),
            viewerInitialIndex: index,
            showViewerIndex: true,
            viewerVisible: true
        });
    },
    onViewerClose(e) {
        const {
            trigger
        } = e.detail;
        console.log(trigger);
        this.setData({
            viewerVisible: false,
        });
    },
    // 跳转到发帖页面
    goToCreatePost: function () {
        wx.navigateTo({
            url: '../post-publish/post-publish',
        });
    },
    // 跳转到搜索页
    goToSearchPage: function () {
        wx.navigateTo({
          url: '../search/search',
        })
    },
    // 关注用户
    followUser: async function (e) {
        const userId = wx.getStorageSync('userId');
        const item = e.currentTarget.dataset.item;
        if(userId == item.userId){
            return toast.show('自己不能关注自己~');
        }
        let params = {
            followerId: userId,
            followedId: item.userId
        }
        let res = await userApi.follow(params);
        if(res.success) {
            let posts = this.data.posts;
            let allPost = posts.filter(p => p.id == item.id || p.userId == item.userId);
            allPost.forEach(ap => {
                ap.isFollowed = res.data;
            });
            this.setData({posts});     
        }
    },
    // 点赞帖子
    likePost: async function (e) {
        const postId = e.currentTarget.dataset.postid;
        let posts = this.data.posts;
        let post = posts.filter(p => p.id == postId)[0];
        let param = {
            userId: wx.getStorageSync('userId'),
            targetId: post.id,
            targetType: 1
        };
        let res = await likeApi.addOrUpdate(param);
        if(res.success) {
            post.isLiked = res.data;
            post.likeCount = post.isLiked ? post.likeCount + 1 : post.likeCount - 1;
            this.setData({
                posts
            });
        }
    }
})