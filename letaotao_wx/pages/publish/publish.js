// pages/publish/publish.js
import categoryApi from '../../api/category/category'
import productApi from '../../api/product/product'
import {
    checkLoginStatus
} from '../../utils/auth';
import {
    validateField,
    isFormValid
} from '../../utils/form';
import imagesApi from '../../api/images/images';
import toast from '../../utils/toast';
import Message from 'tdesign-miniprogram/message/index';
const app = getApp();
Page({
    data: {
        productInfo: {
            name: '',
            description: '',
            categoryId: 0,
            condition: '',
            price: '0.00'
        },
        fileList: [], // 已上传的图片文件列表
        gridConfig: {
            column: 3,
            width: 210,
            height: 200,
        },
        conditionText: '',
        conditionValue: [],
        price: '0.00', // 当前价格
        conditions: [ // 成色下拉选择器的选项
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
        showPricePopup: false,
        keys: [
            '1', '2', '3',
            '4', '5', '6',
            '7', '8', '9',
            '.', '0',
        ],
        selectedCategory: null,
        showCategoryPopup: false,
        categories: [],
        currentCategories: [], // 用于存储当前显示的分类列表
        isEdit: false,
        productId: 0,
        rules: {
            name: {
                validate: value => value && typeof value === 'string' && value.length >= 3,
                message: "商品标题长度不能小于3"
            },
            description: {
                validate: value => value && typeof value === 'string' && value.length >= 5,
                message: "商品描述长度不能小于5"
            },
            categoryId: {
                validate: value => value && typeof value === 'number' && value > 0,
                message: "请选择商品分类"
            },
            condition: {
                validate: value => value && typeof value === 'string' && value.length > 0,
                message: "请选择商品成色"
            },
            price: {
                validate: value => value && Number(value) >= 0.01,
                message: "价格必须大于0.01"
            }
        }
    },
    // 在 onLoad 方法中初始化当前分类
    onLoad(options) {
        this.getCategoryTreeList();
        // 检查是否有id传入，若有则获取帖子详情
        if (options.id) {
            this.setData({
                isEdit: true,
                productId: options.id,
            });
            this.getProductDetails(options.id);
        }
    },
    onShow() {
        if (!checkLoginStatus()) {
            // 如果用户未登录，checkLoginStatus() 内将执行跳转到登录页面的逻辑
            // console.log('用户未登录，跳转到登录页面');
        } else {
            // 用户已经登录，继续执行页面逻辑
            // console.log('用户已登录，继续执行页面逻辑');
        }
    },
    // 获取商品详情
    getProductDetails(productId) {
        productApi.get(productId).then(res => {
            if (res.success) {
                let productInfo = res.data;
                let fileList = productInfo.images.map(img => ({
                    url: img.url,
                    id: img.id,
                    isUploaded: true,
                }));
                // 设置获取的商品详情到页面数据中
                this.setData({
                    productInfo: productInfo,
                    fileList: fileList,
                });
            } else {
                console.error('获取商品详情失败: ', res.message);
            }
        }).catch(err => {
            console.error('请求商品详情出错: ', err);
        });
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    onInputTitleChange(e) {
        const value = e.detail.value;
        this.setData({
            'productInfo.name': value
        }, () => {
            validateField('name', value, this.data.rules);
        });
    },
    onInputDescriptionChange(e) {
        const value = e.detail.value;
        this.setData({
            'productInfo.description': value
        }, () => {
            validateField('description', value, this.data.rules);
        });
    },
    handleImageSuccess(e) {
        const {
            files
        } = e.detail;
 
        this.setData({
            fileList: files.map(file => ({
                ...file,
                isUploaded: file.hasOwnProperty('isUploaded') ? file.isUploaded : false, // 初始化时标记为未上传，如果已有 isUploaded 属性则使用原值，否则设置为 false
            })),
        });
    },
    handleImageRemove(e) {
        const {
            index
        } = e.detail;
        const {
            fileList
        } = this.data;
        if (fileList[index].isUploaded) {
            // 如果图片已上传，需要通知服务器删除
            wx.showModal({
                title: '确认删除',
                content: '您确定要删除这个图片吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.deleteImageFromServer(fileList[index].id);
                        fileList.splice(index, 1); // 从列表中移除图片
                        this.setData({
                            fileList,
                        });
                    }
                },
            });
        } else {
            fileList.splice(index, 1); // 从列表中移除图片
            this.setData({
                fileList,
            });
        }

    },
    handleImageClick(e) {
 
    },
    handleImageDrop(e) {
        const {
            files
        } = e.detail;
        this.setData({
            fileList: files,
        });
    },
    // 删除服务器上的图片
    deleteImageFromServer(id) {
        // 发送请求到服务器删除图片逻辑
        imagesApi.delete(id).then(res => {
            if (res.success) console.log('删除图片成功');
        }).catch(() => {});
    },
    uploadImages(callback) {
        const images = [];

        const {
            fileList
        } = this.data;
        // 筛选出未上传的临时文件
        const tempFiles = fileList.filter(file => !file.isUploaded);
        // 逐个上传图片
        const uploadPromises = tempFiles.map((image, index) => {
            return new Promise((resolve, reject) => {
                wx.uploadFile({
                    url: app.globalData.baseUrl + '/api/lt_images/upload', // 图片上传接口URL
                    filePath: image.url, // 本地临时文件路径
                    name: 'file', // 根据后端接受字段来设置
                    header: {
                        token: wx.getStorageSync('token')
                    },
                    formData: {
                        'module': 'letao-images',
                        'type': 'product'
                    },
                    success: (res) => {
                        const data = JSON.parse(res.data);

                        if (data.success) {
                            // 将服务器返回的图片地址添加到images数组
                            images[index] = data.data;
                            resolve();
                        } else {
                            reject(new Error('上传图片失败'));
                        }
                    },
                    fail: (error) => reject(error)
                });
            });
        });
        Promise.all(uploadPromises).then(() => {
            // 更新fileList中对应项为已上传状态，并存储最终的url
            tempFiles.forEach((file, index) => {
                file.isUploaded = true;
                file.id = images[index].id;
                file.url = images[index].url;
            });
            // 再次调用回调函数，传递已上传的图片
            callback(images);
        }).catch((error) => {
            console.error(error);
            // 上传遇到错误，进行提示或处理
            toast.show('图片上传失败，请重试').catch(() => {});
        });
    },
    getCategoryTreeList() {
        // 获取分类树列表
        categoryApi.getCategoryTreeList().then(res => {
 
            if (res.success) {
                this.setData({
                    categories: res.data
                });
                this.openTopCategories(); // 在页面加载时展示顶层分类
            }
        }).catch(() => {});
    },
    togglePopup() {
        if (!this.data.showCategoryPopup) { // 当当前弹窗是关闭的，即将要打开
            this.openTopCategories(); // 确保打开弹窗前设置为顶层分类
        }
        // 切换弹窗的显示状态
        this.setData({
            showCategoryPopup: !this.data.showCategoryPopup
        });
    },
    /**
     * 展示顶层分类
     */
    openTopCategories() {
        this.setData({
            currentCategories: this.data.categories
        });
    },
    /**
     * 当分类被点击时调用的函数
     */
    enterSubCategories(e) {
        const id = e.currentTarget.dataset.id; // 获取点击项的ID
        const selectedCategory = this.data.currentCategories.find(cat => cat.id === id);

        if (selectedCategory.children && selectedCategory.children.length > 0) {
            this.setCurrentCategories(selectedCategory); // 设置子分类列表
        } else {
            // 如果没有子分类，选择当前分类，并关闭弹窗
            this.selectCategory(selectedCategory);
        }
    },
    /**
     * 设置子分类列表
     */
    setCurrentCategories(category) {
        this.setData({
            currentCategories: category.children
        });
    },
    /**
     * 选择并设置当前分类
     */
    selectCategory(category) {

        this.setData({
            selectedCategory: category,
            showCategoryPopup: false, // 关闭弹窗
            'productInfo.categoryId': category.id,
            'productInfo.categoryName': category.label
        });
    },
    onCategoryVisibleChange(e) {
        this.setData({
            showCategoryPopup: e.detail.visible,
        });
    },
    openCategoryPopup() {
        this.setData({
            showCategoryPopup: true
        });
    },
    onConditionPicker() {
        this.setData({
            conditionVisible: true
        });
    },
    onConditionChange(e) {
        const {
            key
        } = e.currentTarget.dataset;
        const {
            value
        } = e.detail;

        this.setData({
            [`${key}Visible`]: false,
            [`${key}Value`]: value,
            [`${key}Text`]: value.join(' '),
            'productInfo.condition': value.join(' ')
        });
    },
    onConditionCancel(e) {
        const {
            key
        } = e.currentTarget.dataset;
    
        this.setData({
            [`${key}Visible`]: false,
        });
    },
    openPricePopup() {
        if (this.data.price == '0.00') {
            this.data.price = ''
        }
        this.setData({
            showPricePopup: true
        });
    },
    onPriceVisibleChange(e) {
        this.setData({
            showPricePopup: e.detail.visible,
        });
        if (this.data.price == '') {
            this.data.price = '0.00'
        }
    },
    // 处理数字按键输入
    onKeyPress(e) {
        const key = e.currentTarget.dataset.key;
        let {
            price
        } = this.data;
        // 只允许一个小数点
        if (key === '.' && price.includes('.')) {
            return;
        }
        // 价格长度限制
        if (price.length >= 10) {
            return;
        }
        // 价格更新
        this.setData({
            price: price + key
        });
    },
    // 删除按钮逻辑
    onDeleteKeyPress() {
        let {
            price
        } = this.data;
        this.setData({
            price: price.substr(0, price.length - 1)
        });
    },
    // 确定按钮逻辑，添加两位小数
    onConfirmKeyPress() {
        let {
            price
        } = this.data;
        // 过滤掉价格最后的小数点
        if (price.endsWith('.')) {
            price = price.slice(0, -1);
        }
        // 格式化价格为两位小数
        price = Number(price).toFixed(2);
        this.setData({
            price: price,
            showPricePopup: false,
            'productInfo.price': price
        });
    },
    save() {
        if (!isFormValid(this.data.productInfo, this.data.rules)) {
            return;
        }
        const productData = { ...this.data.productInfo };

        this.uploadIfNecessary(productData, (productData) => this.saveOrPublishProduct(productData, 0));
    },
    publish() {
        if (!isFormValid(this.data.productInfo, this.data.rules)) {
            return;
        }
        const productData = { ...this.data.productInfo };

        this.uploadIfNecessary(productData, (productData) => this.saveOrPublishProduct(productData, 1));
    },
    async saveOrPublishProduct(productData, status) {
        try {
            let res;
            if (this.data.isEdit) {
                // 如果是编辑状态，并且当前的状态是草稿，则更新状态为发布
                if (status === 1 && productData.status === 0) {
                    productData.status = 1; // 发布状态
                }
                res = await productApi.update(productData);
            } else {
                // 如果不是编辑状态，则保存新的商品
                productData.publisherId = wx.getStorageSync('userId');
                res = await (status === 0 ? productApi.save : productApi.publish)(productData);
            }
            if (res.success) {
                await this.showToast(res.message);
                wx.redirectTo({
                    url: '../myProduct/myProduct',
                });
            } else {
                await this.showToast(res.message, false);
            }
        } catch (err) {
            console.error(err);
            await this.showToast('操作失败，请重试', 'error');
        }
    },
    uploadIfNecessary(productData, callback) {
        // 若有未上传的图片，则先上传图片
        const fileList = this.data.fileList.filter(file => !file.isUploaded);
        if (fileList.length > 0) {
            this.uploadImages(async (images) => {
                productData['newImages'] = images;
                // 等待图片上传完成后再保存商品
                await callback(productData);
            });
        } else {
            // 如果没有图片需要上传，直接保存商品
            callback(productData);
        }
    },
    async showToast(message, success = true) {
        if (success) {
            await toast.success(message);
        } else {
            await toast.error(message);
        }
    },
})