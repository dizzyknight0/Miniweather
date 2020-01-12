// pages/index/index.js
let messages = require('../../data/messages.js')
let bmap = require('../../lib/bmap-wx.js')
let utils = require('../../utils/utils')
let globalData = getApp().globalData
let SYSTEMINFO = globalData.systeminfo
Page({
  data: {
    message:'',
    searchCity: '',
    enableSearch: true,
    cityDatas: {},
    icons: ['/img/clothing.png', '/img/carwashing.png', '/img/pill.png', '/img/running.png', '/img/sun.png'],
    // 用来清空 input
    searchText: '',
    cityChanged:false,
    },
  onLoad(){
  },
  onShow() {
   this.getCityDatas()
    if (!this.data.cityChanged) {
      this.init({})
    } else {
      this.search(this.data.searchCity)
      this.setData({
        cityChanged: false,
        searchCity: '',
      })
    }
    this.setData({
      message: messages.messages(),
    })
  },
  commitSearch(res) {
    let val = ((res.detail || {}).value || '').replace(/\s+/g, '')//正则表达式去掉string的空格
    this.search(val)
  },
  calcPM(value) {
    if (value > 0 && value <= 50) {
      return {
        val: value,
        desc: '优',
        detail: '',
      }
    } else if (value > 50 && value <= 100) {
      return {
        val: value,
        desc: '良',
        detail: '',
      }
    } else if (value > 100 && value <= 150) {
      return {
        val: value,
        desc: '轻度污染',
        detail: '对敏感人群不健康',
      }
    } else if (value > 150 && value <= 200) {
      return {
        val: value,
        desc: '中度污染',
        detail: '不健康',
      }
    } else if (value > 200 && value <= 300) {
      return {
        val: value,
        desc: '重度污染',
        detail: '非常不健康',
      }
    } else if (value > 300 && value <= 500) {
      return {
        val: value,
        desc: '严重污染',
        detail: '有毒物',
      }
    } else if (value > 500) {
      return {
        val: value,
        desc: '爆表',
        detail: '能出来的都是条汉子',
      }
    }
  },
  getCityDatas() {
    
    let cityDatas = wx.getStorage({
      key: 'cityDatas',
      success: (res) => {
        this.setData({
          cityDatas: res.data,
        })
      },
      
    })
  },

  //获取当前地区的天气数据
    init(params) {
    //  console.log(params)
    let that = this
    let BMap = new bmap.BMapWX({
      ak: globalData.ak,
    })
    BMap.weather({
      location: params.location,
      fail: that.fail,
      success: that.success,
    })
  },

  search(val) {
    if (val === '520' || val === '521') {
      this.setData({
        searchText: '',
      })
      this.dance()
      return
    }
    wx.pageScrollTo({
      scrollTop: 0,
      duration: 300,
    })
    if (val) {
      this.geocoder(val, (loc) => {
        this.init({
          location: `${loc.lng},${loc.lat}`
        })
      })
    }
  },


  
  //获取地理位置后执行，得到天气数据
  success(data) {
 //   console.log(data)
    wx.stopPullDownRefresh()
    let now = new Date()
    // 存下来源数据
    data.updateTime = now.getTime()
    data.updateTimeFormat = utils.formatDate(now, "MM-dd hh:mm")
    let results = data.originalData.results[0] || {}
    data.pm = this.calcPM(results['pm25'])
    // 当天实时温度
    data.temperature = `${results.weather_data[0].date.match(/\d+/g)[2]}`
    wx.setStorage({
      key: 'cityDatas',
      data: data,
    })
    this.setData({
      cityDatas: data,
    })
 //   console.log(data)
  },
  geocoder(address, success) {
    wx.request({
      url: getApp().setGeocoderUrl(address),
      success(res) {
        let data = res.data || {}
        if (!data.status) {
          let location = (data.result || {}).location || {}
          // location = {lng, lat}
          success && success(location)
          
        } else {
          wx.showToast({
            title: data.msg || '网络不给力，请稍后再试',
            icon: 'none',
          })
        }
      },
      fail(res) {
        wx.showToast({
          title: res.errMsg || '网络不给力，请稍后再试',
          icon: 'none',
        })
      },
      complete: () => {
        this.setData({
          searchText: '',
        })
      },
    })
  },
})