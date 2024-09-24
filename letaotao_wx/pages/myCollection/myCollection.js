// pages/myCollection/myCollection.js
import collectionApi from "../../api/collection/collection";
import Message from 'tdesign-miniprogram/message/index';
import toast from "../../utils/toast";
Page({
    data: {
        showSearch: false,
        isEditMode: false,
        tab: 'product',
        searchValue: '',
        targetType: 1,
        collections: [], // 收藏数据列表
        isAllSelected: false, // 是否已全选
    },
    onLoad() {
        this.loadCollections();
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    // 切换搜索栏显示
    toggleSearch() {
        this.setData({
            showSearch: !this.data.showSearch
        });
    },
    // 搜索栏内容变更事件处理
    onSearchChange(e) {
        this.setData({
            searchValue: e.detail.value
        });
    },
    // 搜索确认
    onSearchConfirm() {
        this.loadCollections();;
    },
    // 切换管理模式
    toggleEdit() {
        let {
            isEditMode
        } = this.data;
        this.setData({
            isEditMode: !isEditMode
        }, () => {
            if (!isEditMode) {
                this.setData({
                    isAllSelected: false
                });
            }
        });
    },
    // 切换选项卡
    onTabChange(e) {
        this.setData({
            tab: e.detail.value
        });
        if (e.detail.value === 'product') {
            this.setData({
                targetType: 1,
                collections: []
            });
        } else {
            this.setData({
                targetType: 2,
                collections: []
            });
        }
        this.loadCollections(); // 加载收藏数据
    },
    // 加载商品数据
    loadCollections() {
        let param = {
            targetType: this.data.targetType,
            label: this.data.searchValue
        }
        collectionApi.list(param).then(res => {
            if (res.success) {
                this.setData({
                    collections: res.data
                });
            }
        }).catch(() => {});
    },
    // 下拉菜单选择事件处理
    onDropdownChange: function (e) {
        this.setData({
            'productDropdown.value': e.detail.value
        });
    },
    // 跳转至商品详情页
    goToProductDetail(e) {
        const productId = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: `../product-detail/product-detail?id=${productId}`
        });
    },
    // 跳转至帖子详情页
    goToPostDetail(e) {
        const postId = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: `../post-detail/post-detail?id=${postId}`
        });
    },
    // 商品或帖子选择事件处理
    onSelectItem(e) {
        const {
            id
        } = e.currentTarget.dataset;
        let {
            collections
        } = this.data;
        let collection = collections.filter(c => c.id == id)[0];
        collection.check = !collection.check;
        this.setData({
            collections
        }, () => {
            let selects = collections.filter(c => c.check === true);
            if (selects.length === collections.length) {
                this.setData({
                    isAllSelected: true
                });
            } else {
                this.setData({
                    isAllSelected: false
                });
            }
        });
    },
    // 全选或取消全选
    onSelectAll(e) {
        let {
            collections,
            isAllSelected
        } = this.data;
        isAllSelected = !isAllSelected;
        collections.forEach(c => {
            c.check = isAllSelected;
        });
        this.setData({
            isAllSelected,
            collections
        });
    },
    unfavoriteItems() {
        let {
            collections,
            tab
        } = this.data;
        let select = collections.filter(c => c.check === true);
        if (select.length <= 0) {
            return toast.show('请选择至少一项数据');
        }
        let selectIds, selecNames;
        selectIds = select.map(s => s.id);
        switch (tab) {
            case 'product':
                selecNames = select.map(s => {
                    return s.product.name
                });
                break;
            case 'post':
                selecNames = select.map(s => {
                    return s.post.title
                });
                break;
            default:
                break;
        }
        selecNames = selecNames.join('、');
        wx.showModal({
            title: '取消收藏',
            content: `您确定要取消收藏【${selecNames}】吗？`,
            success: res => {
                if (res.confirm) {
                    this.unFavoriteCollections(selectIds);
                }
            }
        });
    },
    async unFavoriteCollections(selectIds) {
        let res = await collectionApi.cancel(selectIds);
        if (res.success) {
            toast.success(res.message);
            this.loadCollections();
        } else {
            toast.error(res.message);
        }
        this.setData({
            isEditMode: false
        });
    }
})