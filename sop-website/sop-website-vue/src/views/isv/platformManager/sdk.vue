<template>
  <div class="app-container">
    <div v-show="noData">平台暂未提供SDK</div>
    <div v-show="sdkDataList.length > 0" class="base-info">
      <div class="base-info">
        <el-tabs v-model="activeName" type="card" @tab-click="onTabClick">
          <el-tab-pane v-for="item in sdkDataList" :key="item.id" :label="item.name + ` （${item.version}）`" :name="`${item.id}`" :content="item">
            <el-button type="text" @click="downloadFile(item)">下载SDK</el-button>
          </el-tab-pane>
        </el-tabs>
        <h3>调用示例</h3>
        <mavon-editor
          v-model="sdkContent"
          :boxShadow="false"
          :subfield="false"
          defaultOpen="preview"
          :editable="false"
          :toolbarsFlag="false"
        />
      </div>
    </div>
  </div>
</template>
<script>
import { mavonEditor } from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
export default {
  components: { mavonEditor },
  data() {
    return {
      noData: false,
      sdkDataList: [],
      sdkConfigs: [],
      activeName: '',
      sdkContent: ''
    }
  },
  created() {
    this.loadIspSdk()
  },
  methods: {
    loadIspSdk: function() {
      this.get('/portal/isv/listSdk', {}, (resp) => {
        this.sdkDataList = resp.data
        this.noData = this.sdkDataList.length === 0
        if (this.sdkDataList.length > 0) {
          const item = this.sdkDataList[0]
          this.activeName = `${item.id}`
          this.loadSdkFile(item)
        }
      })
    },
    downloadFile: function(row) {
      window.open(row.content)
    },
    onTabClick(tab) {
      const item = tab.$attrs.content
      this.loadSdkFile(item)
    },
    loadSdkFile: function(item) {
      this.sdkContent = item.extContent || '未提供示例'
    }
  }
}
</script>

