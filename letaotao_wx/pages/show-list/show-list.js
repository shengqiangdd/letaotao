// pages/show-list/show-list.js
import productApi from '../../api/product/product'
import postApi from '../../api/post/post'
import userApi from '../../api/user/user'
import toast from '../../utils/toast';
import Message from 'tdesign-miniprogram/message/index';
Page({
    data: {
        tab: 0,
        product: {
            value: 'all',
            options: [{
                    value: 'all',
                    label: '全部商品',
                },
                {
                    value: 'new',
                    label: '最新商品',
                },
            ],
        },
        sorter: {
            value: 'default',
            options: [{
                    value: 'default',
                    label: '默认排序',
                },
                {
                    value: 'price',
                    label: '价格从高到低',
                },
            ],
        },
        condition: {
            value: 'all',
            options: [{
                    value: 'all',
                    label: '全部成色',
                },
                {
                    label: '全新',
                    value: '全新'
                },
                {
                    label: '几乎全新',
                    value: '几乎全新'
                },
                {
                    label: '轻微使用痕迹',
                    value: '轻微使用痕迹'
                },
                {
                    label: '明显使用痕迹',
                    value: '明显使用痕迹'
                }
            ],
        },
        searchValue: '',
        categoryId: 0,
        userId: 0,
        label: '',
        datas: [], // 列表数据
        pageNo: 1, // 当前页码
        pageSize: 10, // 每页数量
        hasMore: true, // 是否还有更多数据
        loading: false,
    },
    onLoad(options) {
        const {
            categoryId,
            label,
            userId
        } = options;
        this.setData({
            categoryId,
            userId: userId ? userId : 0,
            label: label ? label : '',
            searchValue: label
        });
        if(this.label !== undefined || this.label !== '') {
            this.refreshDatas();
        }
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    onSearchChange(value) {
        console.log(value);
    },
    onSearchSubmit(e) {
        this.setData({
            label: e.detail.value
        });
        this.refreshDatas(true);
    },
    searchModelClear() {
        this.data.pageNo = 1;
        this.data.label = '';
    },
    onChangeProduct(e) {
        this.setData({
            'product.value': e.detail.value,
        });
    },
    changeTab(e) {
        this.setData({
            tab: Number.parseInt(e.detail.value)
        })
        this.refreshDatas(true);
    },
    onChangeSorter: function (e) {
        this.setData({
            'sorter.value': e.detail.value
        });
    },
    onChangeCondition: function (e) {
        this.setData({
            'condition.value': e.detail.value
        });
    },
    refreshDatas: function (init = false) {
        // 如果是初始化加载，重置页面数据
        if (init) {
            this.setData({
                datas: [],
                pageNo: 1,
                hasMore: true
            });
        }
        this.loadDatas();
    },
    // 加载商品
    loadDatas: function () {
        if (this.data.loading || !this.data.hasMore) {
            return;
        }
        if (this.data.label === undefine || this.data.label === '') {
            return toast.show('请输入搜索内容');
        }
        this.setData({
            loading: true
        });

        const {
            tab,
            pageNo,
            pageSize,
            label,
            userId
        } = this.data;
        let res;
        let params = {
            pageNo,
            pageSize,
            label
        }
        if(tab === 0) {
            params['productValue'] = this.data.product.value;
            params['sortValue'] = this.data.sorter.value;
            params['conditionValue'] = this.data.condition.value;
        }

        switch (tab) {
            case 0:
                if(userId > 0) {
                    params['userId'] = userId;
                    res = productApi.listByUserId(params);
                } else {
                    res = productApi.list(params);
                }
                break;
            case 1:
                if(userId > 0) {
                    params['userId'] = userId;
                    res = postApi.listByUserId(params);
                } else {
                    res = postApi.list(params);
                }
                break;
            case 2:
                res = userApi.list(params);
            default:
                break;
        }

        res.then(res => {
            const moreDatas = res.data.records || [];
            this.setData({
                datas: this.data.datas.concat(moreDatas), // 合并数据
                pageNo: this.data.pageNo + 1,
                loading: false,
                hasMore: this.data.pageNo < res.data.pages // 更新是否有更多数据
            });
        }).catch(err => {
            console.error('加载数据失败:', err);
            this.setData({
                loading: false
            });
        }).finally(() => {
            wx.stopPullDownRefresh(); // 停止下拉动作
        });
    },
    onSearchValueClear() {
        this.data.label = '';
    },
    onPullDownRefresh: function () {
        this.refreshDatas(true);
    },
    onReachBottom: function () {
        this.refreshDatas();
    },
    goToProductDetail: function (e) {
        const productId = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: `../product-detail/product-detail?id=${productId}`,
        });
    },
    goToPostDetail: function (e) {
        const postId = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: `../post-detail/post-detail?id=${postId}`,
        });
    },
    gotoPersonalHomePage(e) {
        const {
            id
        } = e.currentTarget.dataset;
        wx.navigateTo({
            url: `../personalHome/personalHome?userId=${id}`,
        });
    },
    goToFollowList(e) {
        const {
            id
        } = e.currentTarget.dataset;
        wx.navigateTo({
            url: `../follow-list/follow-list?followType=followers&userId=${id}`,
        });
    },
    followUser: async function (e) {
        let followedId = e.currentTarget.dataset.id;
        let followerId = wx.getStorageSync('userId');
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
            let datas = this.data.datas.map(user => {
                if (user.id === followedId) {
                    user.followerCount = res.data >= 1 ? user.followerCount + 1 : user.followerCount - 1;
                    user.isFollowed = res.data;
                }
                return user;
            });
            this.setData({
                datas
            });
        }
    }
})