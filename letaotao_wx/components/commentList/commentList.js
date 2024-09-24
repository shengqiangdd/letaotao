// components/commentList.js
import commentApi from '../../api/comment/comment';
import likeApi from '../../api/like/like';
import {
    objIsNotNull,
    objIsNull
} from '../../utils/util'
const app = getApp()

Component({
    properties: {
        commentType: Number,
        showAll: {
            type: Boolean,
            value: true
        },
        isChild: {
            type: Boolean,
            value: false
        },
        child: Array,
        product: {
            type: Object,
            observer: function (newVal, oldVal) {
                if (objIsNotNull(newVal) && !app.globalData.hasLoadedComments) {
                    app.globalData.hasLoadedComments = true
                    this.loadComment();
                }
            }
        },
        post: {
            type: Object,
            observer: function (newVal, oldVal) {
                if (objIsNotNull(newVal) && !app.globalData.hasLoadedComments) {
                    app.globalData.hasLoadedComments = true
                    this.loadComment();
                }
            }
        },
        userId: Number,
        newComment: {
            type: Object,
            value: {},
            observer: function (newVal, oldVal) {
                // newVal是父组件传入的新数据
                // 在这里处理数据变化并更新子组件状态
                if (newVal) {
                    const {
                        comments
                    } = this.data;
                    const {
                        showAll
                    } = this.properties;
                    if (!showAll) {
                        comments.push(newVal);
                        this.setData({
                            comments
                        }, () => {
                            this.scrollToBottom();
                        });
                    } else {
                        if (comments.length < 3) {
                            comments.push(newVal);
                            this.setData({
                                comments
                            });
                        }
                    }
                    this.triggerEvent('commentcountchange', {
                        newCommentCount: 1
                    });
                }
            }
        }
    },
    data: {
        comments: [],
        allComments: [],
        shortComments: [],
        shortChild: [],
        commentPopupVisible: false,
        allCommentsPopupVisible: false,
        placeholderText: '',
        inputFocus: false,
        newCommentContent: '',
        editingCommentParentId: 0,
        showAllReplies: {},
        inputContent: '',
        toView: ''
    },
    lifetimes: {
        ready() {
            const {
                isChild,
                child,
                product,
                post
            } = this.properties;
            
            if (!isChild) {
                if (objIsNotNull(product) || objIsNotNull(post)) {
                    this.loadComment();
                }
            } else {
                this.setData({
                    comments: child
                });
            }
        }
    },
    methods: {
        loadComment() {
            const {
                commentType,
                product,
                post,
                userId,
                showAll
            } = this.properties;
            let params = {
                referenceId: commentType === 1 ? product.id : post.id,
                type: commentType,
                userId: userId,
                likeType: commentType === 1 ? 3 : 2
            };
            if (commentType === 1) {
                params['publisherId'] = product.publisherId;
            }
       
            commentApi.list(params).then(res => {
                if (res.success) {
                    let comments = res.data;
                    this.setData({
                        comments: showAll ? comments.slice(0, 3) : comments
                    }, () => {
                        if (!showAll) {
                            this.scrollToBottom();
                        }
                    });
                }
            }).catch(() => {});
        },
        toggleReplies: function (e) {
            const commentId = e.currentTarget.dataset.id;
            const comments = this.data.comments;
            const comment = comments.filter(c => c.id == commentId)[0];
            comment.expand = !comment.expand;
            this.setData({
                comments
            });
        },
        replyComment: function (e) {
            const item = e.currentTarget.dataset.item;
            const {
                commentType,
                product,
                post
            } = this.properties;
            const nickName = item.nickName || (commentType === 1 ? product.nickName : post.nickName);
            console.log(item);
            const parentId = item.id || 0;
            this.setData({
                commentPopupVisible: true,
                placeholderText: "回复" + nickName + "：",
                inputFocus: true,
                editingCommentParentId: parentId
            });
        },
        onCommentInput: function (e) {
            this.setData({
                newCommentContent: e.detail.value
            });
        },
        addComment: async function (inputContent, callback) {
            const {
                editingCommentParentId,
                newCommentContent
            } = this.data;
            const {
                commentType,
                userId,
                product,
                post
            } = this.properties;
            const parentId = (inputContent && typeof inputContent === 'string') ? 0 : (editingCommentParentId || 0);
            const newComment = {
                content: (inputContent && typeof inputContent === 'string') ? inputContent : newCommentContent,
                parentId: Number.parseInt(parentId),
                userId: userId,
                referenceId: commentType === 1 ? product.id : post.id,
                type: commentType
            };
            if(newComment.content === '' || newComment.content === null) {
                return;
            }
            let res = await commentApi.add(newComment);
            if (res.success) {
                // 更新页面数据
                let {
                    product,
                    comments
                } = this.data;
                if (product && product.publisherId === res.data.userId) {
                    res.data.isSeller = true;
                }

                if (!(inputContent && typeof inputContent === 'string')) {
                    const comment = this.findComment(comments, parentId);
                    if (comment) {
                        if (!comment.child) {
                            comment.child = [];
                        }
                        comment.child.push(res.data);
                    } else {
                        comments.push(res.data);
                    }
                    this.setData({
                        comments,
                        commentPopupVisible: false,
                        newCommentContent: "",
                        editingCommentParentId: 0,
                    }, () => {
                        this.triggerEvent('commentcountchange', {
                            newCommentCount: 1
                        });
                    });
                } else {
                    this.setData({
                        inputContent: ''
                    }, () => {
                        if (callback) {
                            callback(res.data);
                        }
                    });
                }
            }
        },
        onCommentCountChange(e) {
            const newCommentCount = e.detail.newCommentCount;
            // 继续向上一级父组件触发事件
            this.triggerEvent('commentcountchange', {
                newCommentCount: newCommentCount
            });
        },
        findComment(comments, parentId) {
            for (let i = 0; i < comments.length; i++) {
                if (comments[i].id === parentId) {
                    return comments[i];
                }
                if (comments[i].child) {
                    let foundComment = this.findComment(comments[i].child, parentId);
                    if (foundComment) {
                        return foundComment;
                    }
                }
            }
            return null;
        },
        sendComment() {
            let {
                inputContent
            } = this.data;
            this.addComment(inputContent, comment => {
                if (comment) {
                    this.selectComponent('.popup-comments').setData({
                        newComment: comment
                    });
                    this.onPopupsCommentAdd(comment);
                }
            });
        },
        onPopupsCommentAdd(comment) {
            let {
                comments
            } = this.data;
            if (comment && comments.length < 3) {
                comments.push(comment);
                this.setData({
                    comments
                });
            }
        },
        likeComment: async function (e) {
            const {
                commentType
            } = this.properties;
            // 点赞留言
            const commentId = e.currentTarget.dataset.commentid;
            let res = await likeApi.addOrUpdate({
                userId: this.properties.userId,
                targetId: commentId,
                targetType: commentType === 1 ? 3 : 2
            });
            if (res.success) {
                // 更新留言的点赞状态
                this.updateCommentLikeState(commentId, res.data);
            }
        },
        updateCommentLikeState(commentId, isLiked) {
            const {
                comments
            } = this.data;
            const comment = comments.filter(c => c.id == commentId)[0];
            comment.isLiked = isLiked;
            comment.likeCount = isLiked > 0 ? comment.likeCount + 1 : comment.likeCount - 1
            this.setData({
                comments
            });
        },
        showAllCommentsPopup() {
            app.globalData.hasLoadedComments = false
            this.setData({
                allCommentsPopupVisible: true
            });
        },
        onInputChange(e) {
            const value = e.detail.value;
            this.setData({
                inputContent: value
            });
        },
        onCommentPopupVisibleChange: function (e) {
            this.setData({
                commentPopupVisible: e.detail.visible
            });
        },
        onAllCommentsPopupVisibleChange: function (e) {
            this.setData({
                allCommentsPopupVisible: e.detail.visible
            });
        },
        scrollToBottom: function () {
            this.triggerEvent('scrollToBottom', {
                toView: 'bottom'
            });
        },
        onScrollToBottom(e) {
            const {
                toView
            } = e.detail;
            this.setData({
                toView
            });
        }
    }
});