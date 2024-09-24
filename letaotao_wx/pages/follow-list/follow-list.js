// pages/follow-list/follow-list.js
import toast from '../../utils/toast'
import userApi from '../../api/user/user'
import Message from 'tdesign-miniprogram/message/index';
Page({
    data: {
        userId: null, // 当前用户编号
        currentUserId: 0,
        userList: [], // 用户列表数据
        followType: null, // 显示类型：'followers'表示粉丝列表，'following'表示关注者列表
        pageNo: 1, // 当前页码
        pageSize: 10, // 每页数量
        hasMore: true, // 是否还有更多数据
        loading: false
    },
    onLoad(options) {
        // 获取传递过来的参数
        const {followType,userId} = options;
        this.setData({currentUserId: wx.getStorageSync('userId')});
        if(followType && userId) {
            this.setData({
                userId: Number.parseInt(userId),
                followType: followType
            },() => {
                this.loadFollows();
            });
        }
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    loadFollows: function () {
        const {loading,hasMore,pageNo,pageSize,followType,userList,userId} = this.data;
        if (loading || !hasMore) {
            return;
        }
        this.setData({
            loading: true
        });
        userApi.followList({
            pageNo: pageNo,
            pageSize: pageSize,
            followType: followType,
            userId: userId
        }).then(res => {
            const moreDatas = res.data.records || [];
            this.setData({
                userList: userList.concat(moreDatas), // 合并数据
                pageNo: pageNo + 1,
                loading: false,
                hasMore: pageNo < res.data.pages // 更新是否有更多数据
            });
        }).catch(err => {
            console.error('加载数据失败:', err);
            this.setData({
                loading: false
            });
        });
    },
    goToFollowList(e) {
        const {
            id
        } = e.currentTarget.dataset;
        wx.navigateTo({
            url: `../personalHome/personalHome?userId=${id}`,
        });
    },
    // 页面触底时的处理
    onReachBottom: function () {
        this.loadFollows(); // 触底时加载更多数据
    },
    followUser: async function (e) {
        let followedId = e.currentTarget.dataset.id;
        let followerId = this.data.userId;
        if (followerId == followedId) {
            toast.show('不能自己关注自己！');
            return;
        }
        let res = await userApi.follow({
            followerId: followerId,
            followedId: followedId
        });
        if (res.success) {
            // 更新用户列表数据，将已关注的用户标记为isFollowed
            let userList = this.data.userList.map(user => {
                if (user.id === followedId) {
                    user.followerCount = res.data >= 1 ? user.followerCount + 1 : user.followerCount - 1;
                    user.isFollowed = res.data;
                }
                return user;
            });
            this.setData({
                userList
            });
        }
    }
})