/*
注册全局方法
 */
import Vue from 'vue'
import { getToken, removeToken } from './auth'
// import ClipboardJS from 'clipboard'
import needle from 'needle'
import md5 from 'js-md5'
import axios from 'axios'

const baseURL = process.env.VUE_APP_BASE_API || `${location.protocol}//${location.host}`
const OPC_USER_TYPE_KEY = 'sop-user-type'

// 创建axios实例
const client = axios.create({
  baseURL: baseURL, // api 的 base_url
  timeout: 60000 // 请求超时时间,60秒
})

Object.assign(Vue.prototype, {
  /**
   * GET请求接口
   * @param uri uri
   * @param data 请求数据
   * @param callback 成功时回调
   * @param errorCallback 失败时回调
   */
  get: function(uri, data, callback, errorCallback) {
    const that = this
    needle.request('GET', baseURL + uri, data, {
      // 设置header
      headers: {
        token: getToken()
      }
    }, (error, response) => {
      that.doResponse(error, response, callback, errorCallback)
    })
  },
  /**
   * 请求接口
   * @param uri uri
   * @param data 请求数据
   * @param callback 成功时回调
   * @param errorCallback 失败时回调
   */
  post: function(uri, data, callback, errorCallback) {
    const that = this
    needle.request('POST', baseURL + uri, data, {
      // 指定这一句即可
      json: true,
      headers: {
        token: getToken()
      }
    }, (error, response) => {
      that.doResponse(error, response, callback, errorCallback)
    })
  },
  request(method, uri, data, headers, isJson, isForm, files, callback) {
    // 如果是文件上传，使用axios，needle上传文件不完美，不支持一个name对应多个文件
    if (files && files.length > 0) {
      this.doMultipart(uri, data, files, headers, callback)
      return
    }
    const that = this
    if (isForm) {
      headers['Content-Type'] = 'application/x-www-form-urlencoded'
    }
    needle.request(method, baseURL + uri, data, {
      // 设置header
      headers: headers,
      json: isJson
    }, (error, response) => {
      callback.call(that, error, response)
    })
  },
  doMultipart(uri, data, files, headers, callback) {
    const that = this
    const formData = new FormData()
    files.forEach(fileConfig => {
      fileConfig.files.forEach(file => {
        formData.append(fileConfig.name, file)
      })
    })
    for (const name in data) {
      formData.append(name, data[name])
    }
    client.post(uri, formData, {
      headers: headers
    }).then(function(response) {
      callback.call(that, null, response)
    }).catch(function(error) {
      callback.call(that, error, null)
    })
  },
  doResponse(error, response, callback, errorCallback) {
    // 成功
    if (!error && response.statusCode === 200) {
      const resp = response.body
      const code = resp.code
      // 未登录
      if (code === '9') {
        this.goLogin()
        return
      }
      if (code === '0') { // 成功
        callback && callback.call(this, resp)
      } else {
        this.$message.error(resp.msg || '请求异常，请查看日志')
        errorCallback && errorCallback.call(this, resp)
      }
    } else {
      this.$message.error('请求异常，请查看日志')
    }
  },
  addRole: function(callback) {
    const that = this
    this.$prompt('请输入角色名称', '创建角色', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /^.{1,64}$/,
      inputErrorMessage: '不能为空且长度在64以内'
    }).then(({ value }) => {
      this.get('isp.role.add', { roleName: value }, function(resp) {
        const data = resp.data
        callback && callback.call(that, data.roleId, data.roleList)
      })
    }).catch(() => {
    })
  },
  loadRole: function(callback) {
    this.get('isp.role.list', {}, resp => {
      callback && callback.call(this, resp.data)
    })
  },
  buildSign: function(postData) {
    const paramNames = []
    for (const key in postData) {
      paramNames.push(key)
    }
    paramNames.sort()
    const paramNameValue = []
    for (let i = 0, len = paramNames.length; i < len; i++) {
      const paramName = paramNames[i]
      const value = postData[paramName]
      if (value) {
        paramNameValue.push(paramName)
        paramNameValue.push(value)
      }
    }
    const secret = this.b
    const source = secret + paramNameValue.join('') + secret
    return md5(source).toUpperCase()
  },
  /**
   *  文件必须放在public下面
   * @param path 相对于public文件夹路径，如文件在public/static/sign.md，填：static/sign.md
   * @param callback 回调函数，函数参数是文件内容
   */
  getFile: function(path, callback) {
    axios.get(path)
      .then(function(response) {
        callback.call(this, response.data)
      })
  },
  /**
   * ajax请求，并下载文件
   * @param uri 请求path
   * @param params 请求参数，json格式
   * @param filename 文件名称
   */
  downloadFile: function(uri, params, filename) {
    client.post(uri, {
      data: encodeURIComponent(JSON.stringify(params)),
      access_token: getToken()
    }).then(response => {
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', filename)
      document.body.appendChild(link)
      link.click()
    })
  },
  /**
   * tip，使用方式：this.tip('操作成功')，this.tip('错误', 'error')
   * @param msg 内容
   * @param type success / info / warning / error
   */
  tip: function(msg, type) {
    this.$message({
      message: msg,
      type: type || 'success'
    })
  },
  tipSuccess: function(msg) {
    this.tip(msg, 'success')
  },
  tipError: function(msg) {
    this.tip(msg, 'error')
  },
  tipInfo: function(msg) {
    this.tip(msg, 'info')
  },
  /**
   * 提醒框
   * @param msg 消息
   * @param okHandler 成功回调
   * @param cancelHandler
   */
  confirm: function(msg, okHandler, cancelHandler) {
    const that = this
    this.$confirm(msg, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      beforeClose: (action, instance, done) => {
        if (action === 'confirm') {
          okHandler.call(that, done)
        } else if (action === 'cancel') {
          if (cancelHandler) {
            cancelHandler.call(that, done)
          } else {
            done()
          }
        } else {
          done()
        }
      }
    }).catch(function() {})
  },
  /**
   * 提示框
   * <pre>
   * this.alert('注册成功', '提示', function() {
      this.goRoute(`/login`)
     })
   * </pre>
   * @param msg
   * @param title
   * @param callback
   */
  alert: function(msg, title, callback) {
    const that = this
    this.$alert(msg, title || '提示', {
      confirmButtonText: '确定',
      callback: action => {
        callback && callback.call(that, action)
      }
    })
  },
  /**
   * 重置表单
   * @param formName 表单元素的ref
   */
  resetForm(formName) {
    const frm = this.$refs[formName]
    frm && frm.resetFields()
  },
  logout: function() {
    this.get('/portal/common/logout', {}, resp => {}, resp => {})
    this.goLogin()
  },
  goLogin() {
    removeToken()
    this.$router.replace({ path: `/login` })
  },
  goRoute: function(path) {
    this.$router.push({ path: path })
  },
  /**
   * array转tree，必须要有id,parentId属性
   * @param arr 数组
   * @param parentId 父节点id，第一次调用传0
   * @returns {Array} 返回树array
   */
  convertTree: function(arr, parentId) {
    if (!arr) {
      return []
    }
    // arr是返回的数据parentId父id
    const temp = []
    const treeArr = arr
    treeArr.forEach((item, index) => {
      if (item.parentId === parentId) {
        // 递归调用此函数
        treeArr[index].children = this.convertTree(treeArr, treeArr[index].id)
        temp.push(treeArr[index])
      }
    })
    return temp
  },
  setAttr: function(key, val) {
    localStorage.setItem(key, val)
  },
  getAttr: function(key) {
    return localStorage.getItem(key)
  },
  setUserType: function(type) {
    this.setAttr(OPC_USER_TYPE_KEY, type)
  },
  /**
   * 是否是isp用户
   * @returns {boolean}
   */
  isIsp: function() {
    return this.getAttr(OPC_USER_TYPE_KEY) === '1'
  },
  isIsv: function() {
    return this.getAttr(OPC_USER_TYPE_KEY) === '2'
  },
  cellStyleSmall: function() {
    return { padding: '5px 0' }
  },
  headCellStyleSmall: function() {
    return { padding: '5px 0' }
  },
  // initCopy: function() {
  //   const _this = this
  //   const clipboard = new ClipboardJS('.copyBtn')
  //   clipboard.on('success', function() {
  //     _this.tipSuccess('复制成功')
  //   })
  //   this.clipboard = clipboard
  // },
  // cleanCopy: function() {
  //   if (this.clipboard) {
  //     this.clipboard.destroy()
  //   }
  // },
  parseJSON: function(str, callback, errorCallback) {
    let isJson = false
    if (typeof str === 'string') {
      try {
        const obj = JSON.parse(str)
        isJson = (typeof obj === 'object') && obj
        if (isJson) {
          callback.call(this, obj)
        }
      } catch (e) {
        isJson = false
      }
    }
    if (!isJson) {
      errorCallback.call(this)
    }
  },
  isObject: function(obj) {
    return Object.prototype.toString.call(obj) === '[object Object]'
  },
  isArray: function(obj) {
    return Object.prototype.toString.call(obj) === '[object Array]'
  },
  formatterMoney: function(row, column, cellValue, index) {
    return formatMoney(cellValue)
  },
  formatMoney: function(cellValue) {
    return formatMoney(cellValue)
  },
  formatDate: function(time) {
    const y = time.getFullYear()
    const m = time.getMonth() + 1
    const d = time.getDate()
    const h = time.getHours()
    const mm = time.getMinutes()
    const s = time.getSeconds()
    return `${y}-${this._add0(m)}-${this._add0(d)} ${this._add0(h)}:${this._add0(mm)}:${this._add0(s)}`
  },
  _add0: function(m) {
    return m < 10 ? '0' + m : m
  }
})

const formatMoney = function(cellValue) {
  return '￥' + (cellValue / 100).toFixed(2)
}
