<template>
  <div>
    <h2>{{ docInfo.summary }}</h2>
    <el-table
      :data="[{ methodLabel: '接口名(method)', methodValue: docInfo.name, versionLabel: '版本号(version)', versionValue: docInfo.version }]"
      border
      :cell-style="baseInfoCellStyle"
      :show-header="false"
    >
      <el-table-column prop="methodLabel" align="center" width="130">
        <template slot-scope="scope"><span class="api-info">{{ scope.row.methodLabel }}</span></template>
      </el-table-column>
      <el-table-column prop="methodValue" />
      <el-table-column prop="versionLabel" align="center" width="130">
        <template slot-scope="scope"><span class="api-info">{{ scope.row.versionLabel }}</span></template>
      </el-table-column>
      <el-table-column prop="versionValue" width="120" />
    </el-table>
    <h3>接口描述</h3>
    <div class="doc-overview">{{ docInfo.description || docInfo.title }}</div>
    <h3>请求地址</h3>
    <el-table
      :data="[{ envLabel: '环境', envValue: '正式环境', urlLabel: '请求地址', urlValue: urlProd }]"
      border
      :cell-style="baseInfoCellStyle"
      :show-header="false"
    >
      <el-table-column align="center" width="100">
        <template slot-scope="scope"><span class="api-info">{{ scope.row.envLabel }}</span></template>
      </el-table-column>
      <el-table-column prop="envValue" width="140" />
      <el-table-column align="center" width="100">
        <template slot-scope="scope"><span class="api-info">{{ scope.row.urlLabel }}</span></template>
      </el-table-column>
      <el-table-column prop="urlValue" />
    </el-table>
    <h3>请求方法</h3>
    <div class="doc-request-method">
      {{ docInfo.httpMethodList && docInfo.httpMethodList.join(' / ').toUpperCase() }}
    </div>
    <h2>请求参数</h2>
    <h3>公共请求参数</h3>
    <el-table
      :data="commonParams"
      :cell-style="cellStyleSmall()"
      :header-cell-style="headCellStyleSmall()"
      border
    >
      <el-table-column
        prop="name"
        label="名称"
        width="200"
      />
      <el-table-column
        prop="type"
        label="类型"
        width="100"
      />
      <el-table-column
        prop="must"
        label="必须"
        width="60"
      >
        <template slot-scope="scope">
          <span :class="scope.row.must ? 'danger' : ''">{{ scope.row.must ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="description"
        label="描述"
      >
        <template slot-scope="scope">
          {{ scope.row.description }}
          <span v-if="scope.row.name === 'sign'">，
            <router-link target="_blank" to="/help?id=sign">
              <el-button type="text">签名算法介绍</el-button>
            </router-link>
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="example"
        label="示例值"
      />
    </el-table>
    <h3>业务请求参数</h3>
    <parameter-table :data="docInfo.requestParameters" />
    <h2>响应参数</h2>
    <h3>公共响应参数</h3>
    <el-table
      :data="commonResult"
      :cell-style="cellStyleSmall()"
      :header-cell-style="headCellStyleSmall()"
      border
    >
      <el-table-column
        prop="name"
        label="名称"
        width="200"
      />
      <el-table-column
        prop="type"
        label="类型"
        width="100"
      />
      <el-table-column
        prop="description"
        label="描述"
      />
      <el-table-column
        prop="example"
        label="示例值"
      />
    </el-table>
    <h3>业务响应参数</h3>
    <parameter-table :data="docInfo.responseParameters" />
    <h3>响应示例</h3>
    <pre class="normal-text">{{ JSON.stringify(responseSuccessExample, null, 4) }}</pre>
    <h3>错误示例</h3>
    <pre class="normal-text">{{ JSON.stringify(responseErrorExample, null, 4) }}</pre>
    <h2>业务错误码</h2>
    <router-link target="_blank" to="/code">
      <el-button type="text">公共错误码</el-button>
    </router-link>
    <el-table
      :data="docInfo.bizCodeList"
      border
      :cell-style="cellStyleSmall()"
      :header-cell-style="headCellStyleSmall()"
    >
      <el-table-column
        prop="code"
        label="sub_code（错误码）"
        width="300"
      />
      <el-table-column
        prop="msg"
        label="sub_msg（错误描述）"
      />
      <el-table-column
        prop="solution"
        label="解决方案"
      />
    </el-table>
  </div>
</template>

<script>
import ParameterTable from '@/components/ParameterTable'
export default {
  name: 'DocView',
  components: { ParameterTable },
  props: {
    item: {
      type: Object,
      default: () => {}
    },
    urlProd: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      active: 'info',
      baseInfoCellStyle: (row) => {
        if (row.columnIndex === 0 || row.columnIndex === 2) {
          return { padding: '5px 0', background: '#f5f7fa' }
        } else {
          return { padding: '5px 0' }
        }
      },
      commonParams: [],
      commonResult: [],
      docBaseInfoData: [],
      docInfo: {
        summary: '',
        httpMethodList: [],
        requestParameters: [],
        responseParameters: [],
        bizCodes: []
      },
      responseSuccessExample: {},
      responseErrorExample: {
        error_response: {
          request_id: '0d27836fcac345729176359388aeeb74',
          code: '40004',
          msg: '业务处理失败',
          sub_code: 'isv.name-error',
          sub_msg: '姓名错误'
        }
      }
    }
  },
  watch: {
    item(newVal) {
      this.initItem(newVal)
    }
  },
  created() {
    this.getFile('static/params.json', (json) => {
      this.commonParams = json.commonParams
      this.commonResult = json.commonResult
    })
  },
  methods: {
    initItem(item) {
      this.setData(item)
    },
    setData: function(data) {
      this.docInfo = data
      this.createResponseExample(data)
    },
    createResponseExample: function(data) {
      const ret = {}
      const responseData = {
        request_id: '4b8e7ca9cbcb448491df2f0120e49b9d',
        code: '10000',
        msg: 'success'
      }
      ret[this.getResponseNodeName()] = responseData
      const bizRet = this.createExample(data.responseParameters)
      for (const key in bizRet) {
        responseData[key] = bizRet[key]
      }
      this.responseSuccessExample = ret
    },
    createExample: function(params) {
      const responseJson = {}
      for (let i = 0; i < params.length; i++) {
        const row = params[i]
        if (row.in === 'header') {
          continue
        }
        let val
        // 如果有子节点
        if (row.refs && row.refs.length > 0) {
          const childrenValue = this.createExample(row.refs)
          // 如果是数组
          if (row.type === 'array') {
            val = [childrenValue]
          } else {
            val = childrenValue
          }
        } else {
          // 单值
          val = row.paramExample
        }
        responseJson[row.name] = val
      }
      const isOneArray = Object.keys(responseJson).length === 1 && this.isArray(Object.values(responseJson)[0])
      if (isOneArray) {
        return Object.values(responseJson)[0]
      }
      return responseJson
    },
    getResponseNodeName: function() {
      const name = this.docInfo.name
      return name.replace(/\./g, '_') + '_response'
    }
  }
}
</script>

<style scoped>
.api-info {font-weight: bold;}
.doc-overview {margin-top: 20px;margin-bottom: 30px;color: #666;font-size: 14px;}
.doc-request-method {margin-bottom: 20px;color: #666;font-size: 14px;}
</style>
