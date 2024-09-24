// pages/personalDetail/personalDetail.js
Page({

  data: {
    userInfo: null
  },
  onLoad(options) {
    let {userInfo} = options;
    if(userInfo) {
        this.setData({userInfo: JSON.parse(decodeURIComponent(userInfo))});
    }
  }
})