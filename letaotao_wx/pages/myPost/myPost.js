// pages/myPost/myPost.js
import postApi from '../../api/post/post'
import likeApi from '../../api/like/like'
import toast from '../../utils/toast';
import Message from 'tdesign-miniprogram/message/index';
Page({
    data: {
        posts: [],
        pageNo: 1,
        pageSize: 10,
        currentStatus: 1, // 默认为'已发布'
        hasMore: true,
        loading: false
    },
    onLoad(options) {
        this.loadPosts();
    },
    onTabChange: function (event) {
        const status = event.currentTarget.dataset.status;
        if (status !== this.data.currentStatus) {
            this.setData({
                pageNo: 1,
                posts: [],
                hasMore: true,
                currentStatus: status,
            });
            this.loadPosts();
        }
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    loadPosts: function () {
        if (this.data.loading || !this.data.hasMore) {
            return;
        }

        let params = {
            status: this.data.currentStatus,
            userId: wx.getStorageSync('userId'),
            pageNo: this.data.pageNo,
            pageSize: this.data.pageSize,
            currentUserId: wx.getStorageSync('userId')
        }
        this.setData({
            loading: true
        });
        // 使用接口请求获取帖子数据
        postApi.listByMe(params).then(res => {
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
    // 页面触底时的处理
    onReachBottom: function () {
        this.loadPosts(); // 触底时加载更多商品
    },
    navigateToPostDetail(event) {
        const postId = event.currentTarget.dataset.id;
        wx.navigateTo({
            url: `../post-detail/post-detail?id=${postId}`,
        });
    },
    showActionSheet(e) {
        const postId = e.currentTarget.dataset.id;
        wx.showActionSheet({
            itemList: ['编辑'],
            success(res) {
                wx.navigateTo({
                    url: `../post-publish/post-publish?id=${postId}`,
                });
            },
        });
    },
    confirmDeletePost(event) {
        const postId = event.currentTarget.dataset.id;
        wx.showModal({
            title: '确认删除',
            content: '您确定要删除这篇帖子吗？',
            success: res => {
                if (res.confirm) {
                    this.deletePost(postId);
                }
            }
        });
    },
    async deletePost(postId) {
        let res = await postApi.delete(postId);
        if (res.success) {
            toast.show(res.message).then(() => {
                this.setData({
                    pageNo: 1,
                    hasMore: true,
                    posts: []
                }, () => {
                    this.loadPosts(true);
                });

            }).catch(() => {});
        }
    },
    async toggleLikePost(e) {
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
    },
    navigateToCreatePost() {
        wx.navigateTo({
            url: '../post-publish/post-publish',
        });
    }
})