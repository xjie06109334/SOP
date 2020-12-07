<template>
  <div class="app-container">
    <h3>基本信息</h3>
    <div class="base-info">
      <el-form
        class="text-form"
        label-width="150px">
        <el-form-item label="应用ID（appId）">
          {{ isvInfo.appId }}
        </el-form-item>
        <el-form-item label="应用公钥">
          <span v-if="isvInfo.isUploadPublicKey" class="success">已上传 <el-button type="text" @click="showKeys(isvInfo)">修改/查看</el-button></span>
          <span v-else class="warning">未上传 <el-button type="text" @click="showKeys(isvInfo)">上传</el-button></span>
        </el-form-item>
      </el-form>
      <el-dialog
        :visible.sync="keysDlgViewShow"
      >
        <span slot="title">
          配置公钥
        </span>
        <router-link target="_blank" to="/help?id=keys">
          <el-button type="text" icon="el-icon-question">公私钥介绍</el-button>
        </router-link>
        <fieldset>
          <legend>应用公钥</legend>
          <el-alert class="el-alert-tip" type="success" :closable="false">
            <span slot="title">公钥已上传 <el-link :underline="false" type="primary" @click="reUploadPublicKey">重新上传</el-link></span>
          </el-alert>
          <div class="key-content"> {{ keys.publicKeyIsv }} </div>
        </fieldset>
        <br>
        <fieldset>
          <legend>平台公钥</legend>
          <div class="key-content"> {{ keys.publicKeyPlatform }} </div>
        </fieldset>
        <div slot="footer" class="dialog-footer">
          <el-button @click="keysDlgViewShow = false">关 闭</el-button>
        </div>
      </el-dialog>
      <!-- 上传dlg -->
      <el-dialog
        :visible.sync="keysUploadDlgShow"
        :close-on-click-modal="false"
      >
        <span slot="title">
          配置公钥
        </span>
        <router-link target="_blank" to="/help?id=keys">
          <el-button type="text" icon="el-icon-question">公私钥介绍</el-button>
        </router-link>
        <fieldset>
          <legend>上传应用公钥</legend>
          <el-alert class="el-alert-tip" title="生成一对公私钥，将公钥填在这里，私钥妥善保存，用于接口调用" :closable="false" />
          <el-form
            ref="keysForm"
            :model="keys"
          >
            <el-form-item prop="publicKeyIsv" :rules="[{ required: true, message: '请填写公钥', trigger: 'blur' }]">
              <el-input
                v-model="keys.publicKeyIsv"
                type="textarea"
                :rows="5"
                placeholder="公钥，去除-----BEGIN PUBLIC KEY-----  -----END PUBLIC KEY-----部分"
              />
            </el-form-item>
          </el-form>
        </fieldset>
        <div slot="footer" class="dialog-footer">
          <el-button :loading="keysBtnLoading" type="primary" @click="onUploadPublicKey">上 传</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      isvInfo: {
        appId: '',
        isUploadPublicKey: 0
      },
      keys: {
        isUploadPublicKey: 0,
        publicKeyIsv: '',
        publicKeyPlatform: '',
        env: 0
      },
      keysBtnLoading: false,
      keysDlgViewShow: false,
      keysUploadDlgShow: false
    }
  },
  created() {
    this.loadIsvInfo()
  },
  methods: {
    loadIsvInfo: function() {
      this.get('/portal/isv/getIsvPortal', {}, (resp) => {
        this.isvInfo = resp.data
      })
    },
    showKeys: function(item) {
      Object.assign(this.keys, {
        isUploadPublicKey: item.isUploadPublicKey
      })
      if (!this.keys.isUploadPublicKey) {
        this.keys.publicKeyIsv = ''
        this.keysUploadDlgShow = true
      } else {
        this.loadPublicKey()
      }
    },
    reUploadPublicKey: function() {
      this.keysDlgViewShow = false
      this.keys.isUploadPublicKey = 0
      this.keys.publicKeyIsv = ''
      this.keysUploadDlgShow = true
    },
    loadPublicKey: function() {
      this.get('/portal/isv/getPublicKey', {}, resp => {
        this.showPublicKey(resp.data)
      })
    },
    showPublicKey(data) {
      Object.assign(this.keys, data)
      this.keysDlgViewShow = true
    },
    onUploadPublicKey: function() {
      this.$refs.keysForm.validate(valid => {
        if (valid) {
          this.keysBtnLoading = true
          this.post('/portal/isv/uploadPublicKey', { publicKeyIsv: this.keys.publicKeyIsv }, resp => {
            this.keysBtnLoading = false
            this.keysUploadDlgShow = false
            this.showPublicKey(resp.data)
            this.loadIsvInfo()
          }, resp => {
            this.keysBtnLoading = false
            this.tipError(resp.msg)
          })
        }
      })
    }
  }
}
</script>
