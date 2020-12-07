<template>
  <div class="app-container">
    <el-form size="mini">
      <el-form-item>
        <el-button type="primary" icon="el-icon-upload" @click="onAddSdk">发布SDK</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="list"
      border
    >
      <el-table-column
        prop="name"
        label="SDK"
        width="120"
      />
      <el-table-column
        prop="version"
        label="版本"
        width="120"
      />
      <el-table-column
        prop="content"
        label="下载地址"
      >
        <template slot-scope="scope">
          <el-link type="primary" :href="scope.row.content" target="_blank">{{ scope.row.content }}</el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        width="150"
        align="center"
      >
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="onSdkUpdate(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" @click="onSdkDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--dialog-->
    <el-dialog
      :title="sdkDlgTitle"
      :visible.sync="sdkDlgAddShow"
      :close-on-click-modal="false"
      @close="resetForm('sdkAddForm')"
    >
      <el-form
        ref="sdkAddForm"
        :model="sdkFormAddData"
        :rules="sdkFormRule"
        label-width="100px"
      >
        <el-form-item prop="name" label="选择语言">
          <el-select
            v-model="sdkFormAddData.name"
            placeholder="请选择"
          >
            <el-option
              v-for="item in sdkConfigs"
              :key="item.name"
              :label="item.name"
              :value="item.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="version" label="版本">
          <el-input v-model="sdkFormAddData.version" maxlength="30" show-word-limit placeholder="如：1.0" />
        </el-form-item>
        <el-form-item prop="content" label="下载地址">
          <el-input v-model="sdkFormAddData.content" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item prop="extContent" label="调用示例">
          <el-input v-model="sdkFormAddData.extContent" type="textarea" :rows="12" placeholder="填写SDK调用示例代码，支持markdown语法" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sdkDlgAddShow = false">取 消</el-button>
        <el-button type="primary" @click="onSubmitForm">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
const appFormDataInit = function() {
  return {
    id: 0,
    name: '',
    version: '',
    content: '',
    extContent: ''
  }
}
export default {
  data() {
    return {
      searchFormData: {},
      sdkDownloadConfig: [],
      sdkConfigs: [],
      sdkDlgTitle: '',
      sdkDlgAddShow: false,
      sdkFormUpdateData: appFormDataInit(),
      sdkFormAddData: appFormDataInit(),
      sdkFormLoading: false,
      sdkFormRule: {
        name: [
          { required: true, message: '请选择语言', trigger: 'blur' }
        ],
        version: [
          { required: true, message: '请填版本', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请填写URL', trigger: 'blur' }
        ],
        extContent: [
          { required: true, message: '请填写调用示例', trigger: 'blur' }
        ]
      },
      downloadUrl: '',
      list: []
    }
  },
  created() {
    this.loadLangSelector()
    this.loadTable()
  },
  methods: {
    loadLangSelector: function() {
      this.getFile(`static/sdkConfig.json?q=${new Date().getTime()}`, (content) => {
        this.sdkConfigs = content.langList
      })
    },
    loadTable: function() {
      this.post('isp.sdk.list', this.searchFormData, resp => {
        this.list = resp.data
      })
    },
    onSizeChange: function(size) {
      this.searchFormData.pageSize = size
      this.loadTable()
    },
    onPageIndexChange: function(pageIndex) {
      this.searchFormData.pageIndex = pageIndex
      this.loadTable()
    },
    onAddSdk: function() {
      this.sdkDlgTitle = '添加SDK'
      this.sdkFormAddData = appFormDataInit()
      this.sdkDlgAddShow = true
    },
    onSdkUpdate: function(row) {
      this.sdkDlgTitle = '修改SDK'
      this.sdkFormAddData = appFormDataInit()
      Object.assign(this.sdkFormAddData, row)
      this.sdkDlgAddShow = true
    },
    onSdkDelete: function(row) {
      this.confirm(`确认要删除【${row.name}】吗？`, (done) => {
        this.post('isp.sdk.delete', { id: row.id }, resp => {
          done()
          this.tip('删除成功')
          this.loadTable()
        })
      })
    },
    onSubmitForm: function() {
      this.$refs.sdkAddForm.validate((valid) => {
        if (valid) {
          const uri = this.sdkFormAddData.id ? 'isp.sdk.update' : 'isp.sdk.add'
          this.post(uri, this.sdkFormAddData, function() {
            this.sdkDlgAddShow = false
            this.loadTable()
          })
        }
      })
    }
  }
}
</script>
