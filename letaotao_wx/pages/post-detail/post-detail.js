// pages/post-detail/post-detail.js
import postApi from '../../api/post/post';
import userApi from '../../api/user/user';
import commentApi from '../../api/comment/comment'
import likeApi from '../../api/like/like'
import collectionApi from '../../api/collection/collection'
import Message from 'tdesign-miniprogram/message/index';
Page({
    data: {
        post: {},
        userId: 0,
        popupVisible: false, // 弹出框的可见性和输入框的焦点
        inputFocus: false,
        placeholderText: "", // 输入框的占位符
        newCommentContent: "", // 新的评论的内容
        editingCommentParentId: 0,
    },
    onLoad: function (options) {
        this.setData({
            userId: wx.getStorageSync('userId')
        });
        if (options.id) {
            const app = getApp()
            if(app.globalData.hasLoadedComments) {
                app.globalData.hasLoadedComments = false
            }
            // 加载帖子数据
            this.getPost(options.id);
        }
    },
    getPost(id) {
        postApi.get(id).then(res => {
            if (res.success) {
                this.setData({
                    post: res.data
                });

            }
        }).catch({});
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    goToPersonalHomePage() {
        wx.navigateTo({
          url: `../personalHome/personalHome?userId=${this.data.post.userId}`,
        });
    },
    followUser: async function () {
        const userId = this.data.userId;
        const post = this.data.post;
        if (userId == post.userId) {
            return toast.show('自己不能关注自己~');
        }
        let params = {
            followerId: userId,
            followedId: post.userId
        }
        let res = await userApi.follow(params);
        if (res.success) {
            post.isFollowed = res.data;
            this.setData({
                post
            });
        }
    },
    onCommentCountChange(e) {
        const {
            newCommentCount
        } = e.detail;

        let post = this.data.post;
        post.commentCount += newCommentCount;
        this.setData({
            post
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
        const nickName = this.data.post.nickName;
        const parentId = 0;
        this.setData({
            popupVisible: true,
            placeholderText: "给" + nickName + "评论：",
            inputFocus: true,
            editingCommentParentId: parentId
        });
    },
    // 输入新评论的内容 
    handleInput: function (e) {
        this.setData({
            newCommentContent: e.detail.value
        });
    },
    // 添加评论
    addComment: async function () {
        const {
            editingCommentParentId,
            newCommentContent,
            userId,
            post
        } = this.data;
        const parentId = editingCommentParentId || 0;
        // 构造新评论的数据
        const newComment = {
            content: newCommentContent,
            parentId: Number.parseInt(parentId),
            userId: userId,
            referenceId: post.id,
            type: 2,
        };
        if(newComment.content === '' || newComment.content === null) {
            return;
        }

        // 发布评论
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
    // 点赞或取消赞帖子
    toggleLikePost: async function () {
        let post = this.data.post;
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
                post
            });
        }
    },
    // 收藏或取消收藏帖子
    toggleFavorite: async function () {
        let post = this.data.post;
        let param = {
            userId: wx.getStorageSync('userId'),
            targetId: post.id,
            targetType: 2
        };
        let res = await collectionApi.addOrUpdate(param);
        if (res.success) {
            post.isFavorite = res.data;
            post.collectCount = post.isFavorite ? post.collectCount + 1 : post.collectCount - 1;
            this.setData({
                post
            });
        }
    },

});