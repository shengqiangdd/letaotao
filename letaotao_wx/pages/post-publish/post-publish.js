// pages/post-publish/post-publish.js
import {
    checkLoginStatus
} from '../../utils/auth';
import {
    validateField,
    isFormValid
} from '../../utils/form';
import postApi from '../../api/post/post';
import imagesApi from '../../api/images/images';
import toast from '../../utils/toast';
import Message from 'tdesign-miniprogram/message/index';
const app = getApp();
Page({
    data: {
        postInfo: {
            title: '',
            content: ''
        },
        fileList: [], // 已上传的图片文件列表
        gridConfig: {
            column: 3,
            width: 210,
            height: 200,
        },
        isEdit: false, // 是否是编辑状态
        postId: null, // 帖子id
        rules: {
            title: {
                validate: value => value && typeof value === 'string' && value.length >= 3,
                message: "帖子标题长度不能小于3"
            },
            content: {
                validate: value => value && typeof value === 'string' && value.length >= 5,
                message: "帖子内容长度不能小于5"
            }
        }
    },
    onLoad(options) {
        // 检查是否有id传入，若有则获取帖子详情
        if (options.id) {
            this.setData({
                isEdit: true,
                postId: options.id,
            });
            this.getPostDetails(options.id);
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
    // 获取帖子详情
    getPostDetails(postId) {
        postApi.get(postId).then(res => {
            if (res.success) {
                let postInfo = res.data;
                let fileList = postInfo.images.map(img => ({
                    url: img.url,
                    id: img.id,
                    isUploaded: true,
                }));
                // 设置获取的帖子详情到页面数据中
                this.setData({
                    postInfo: postInfo,
                    fileList: fileList,
                });
            } else {
                console.error('获取帖子详情失败: ', res.message);
            }
        }).catch(err => {
            console.error('请求帖子详情出错: ', err);
        });
    },
    showNewMessage(obj) {
        Message.info(obj);
    },
    onInputTitleChange(e) {
        const value = e.detail.value;
        this.setData({
            'postInfo.title': value
        }, () => {
            validateField('title', value, this.data.rules);
        });
    },
    onInputContentChange(e) {
        const value = e.detail.value;
        this.setData({
            'postInfo.content': value
        }, () => {
            validateField('content', value, this.data.rules);
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
                        'type': 'post'
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
    save() {
        if (!isFormValid(this.data.postInfo, this.data.rules)) {
            return;
        }

        const postData = {
            ...this.data.postInfo
        }

        this.uploadUnfinishedImages(postData, (postData) => this.saveOrPublishPost(postData, 0)); // 0 表示草稿状态
    },
    publish() {
        if (!isFormValid(this.data.postInfo, this.data.rules)) {
            return;
        }

        const postData = {
            ...this.data.postInfo
        }

        this.uploadUnfinishedImages(postData, (postData) => this.saveOrPublishPost(postData, 1)); // 1 表示发布状态
    },
    async saveOrPublishPost(postData, status) {
        let res;
        try {
            // 处理编辑状态
            if (this.data.isEdit) {
                // 如果是编辑状态，并且当前的状态是草稿，则更新状态为发布
                if (status === 1 && postData.status === 0) {
                    postData['status'] = 1;
                }
                res = await postApi.update(postData);
            } else {
                // 如果不是编辑状态，则发布新的帖子
                postData['userId'] = wx.getStorageSync('userId');
                res = await (status === 0 ? postApi.save : postApi.publish)(postData);
            }
            if (res.success) {
                await toast.success(res.message);
                wx.redirectTo({
                    url: '../myPost/myPost',
                });
            } else {
                await toast.error(res.message);
            }
        } catch (err) {
            console.error(err);
            await toast.show('操作失败，请重试');
        }
    },
    uploadUnfinishedImages(postData, callback) {
        // 若有未上传的图片，则先上传图片
        const fileList = this.data.fileList.filter(file => !file.isUploaded);
        if (fileList.length > 0) {
            this.uploadImages(async (images) => {
                postData['newImages'] = images;
                // 等待图片上传完成后再保存帖子
                await callback(postData);
            });
        } else {
            // 如果没有图片需要上传，直接保存帖子
            callback(postData);
        }
    },
})