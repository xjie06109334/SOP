<template>
  <div class="app-container">
    <el-form :inline="true" :model="searchFormData" class="demo-form-inline" size="mini" @submit.native.prevent>
      <el-form-item label="接口名">
        <el-input v-model="searchFormData.routeId" :clearable="true" style="width: 250px;" />
      </el-form-item>
      <el-form-item label="serviceId">
        <el-input v-model="searchFormData.serviceId" :clearable="true" style="width: 250px;" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" native-type="submit" @click="loadTable">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="pageInfo.list"
      row-key="id"
      lazy
      empty-text="无数据"
      :load="loadInstanceMonitorInfo"
    >
      <el-table-column
        fixed
        prop="instanceId"
        label="网关实例"
        width="200"
      >
        <template slot-scope="scope">
          <span v-if="!scope.row.children">{{ scope.row.instanceId }}</span>
        </template>
      </el-table-column>
      <el-table-column
        fixed
        prop="name"
        label="接口名 (版本号)"
        width="200"
      >
        <template slot-scope="scope">
          {{ scope.row.name + (scope.row.version ? ' (' + scope.row.version + ')' : '') }}
        </template>
      </el-table-column>
      <el-table-column
        prop="serviceId"
        label="serviceId"
        width="150"
      />
      <el-table-column
        prop="maxTime"
        label="最大耗时(ms)"
      >
        <template slot="header">
          最大耗时(ms)
          <el-tooltip content="耗时计算：签名验证成功后开始，应用返回结果后结束" placement="top">
            <i class="el-icon-question" style="cursor: pointer"></i>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        prop="minTime"
        label="最小耗时(ms)"
      />
      <el-table-column
        prop="avgTime"
        label="平均耗时(ms)"
      >
        <template slot-scope="scope">
          {{ scope.row.avgTime.toFixed(1) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="totalRequestCount"
        label="总调用次数"
      />
      <el-table-column
        prop="successCount"
        label="成功次数"
      />
      <el-table-column
        prop="errorCount"
        label="失败次数"
      />
      <el-table-column
        prop="unsolvedErrorCount"
        label="未解决错误"
      >
        <template slot-scope="scope">
          <el-link
            v-if="scope.row.unsolvedErrorCount > 0"
            :underline="false"
            type="danger"
            style="text-decoration: underline;"
            @click="onShowErrorDetail(scope.row)"
          >
            {{ scope.row.unsolvedErrorCount }}
          </el-link>
          <span v-if="scope.row.unsolvedErrorCount === 0">0</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      style="margin-top: 5px"
      :current-page="searchFormData.pageIndex"
      :page-size="searchFormData.pageSize"
      :page-sizes="[5, 10, 20, 40]"
      :total="pageInfo.total"
      layout="total, sizes, prev, pager, next"
      @size-change="onSizeChange"
      @current-change="onPageIndexChange"
    />
    <!-- dialog -->
    <el-dialog
      :title="errorMsgData.title"
      :visible.sync="logDetailVisible"
      :close-on-click-modal="false"
      width="70%"
      @close="onCloseErrorDlg"
    >
      <el-alert
        title="修复错误后请标记解决"
        :closable="false"
        class="el-alert-tip"
      />
      <el-table
        :data="errorMsgData.pageInfo.rows"
        empty-text="无错误日志"
      >
        <el-table-column
          type="expand"
        >
          <template slot-scope="props">
            <el-input v-model="props.row.errorMsg" type="textarea" :rows="8" readonly />
          </template>
        </el-table-column>
        <el-table-column
          prop="errorMsg"
          label="错误内容"
        >
          <template slot-scope="props">
            <span v-if="props.row.errorMsg.length > 50">{{ props.row.errorMsg.substring(0, 50) }}...</span>
            <span v-else>{{ props.row.errorMsg }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="instanceId"
          label="实例ID"
          width="150px"
        />
        <el-table-column
          prop="count"
          label="报错次数"
          width="80px"
        />
        <el-table-column
          prop="gmtModified"
          label="报错时间"
          width="160px"
        />
        <el-table-column
          label="操作"
          width="120"
        >
          <template slot-scope="scope">
            <el-link type="primary" @click="onSolve(scope.row)">标记解决</el-link>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top: 5px"
        :current-page="errorMsgFormData.pageIndex"
        :page-size="errorMsgFormData.pageSize"
        :page-sizes="[5, 10, 20, 40]"
        :total="errorMsgData.pageInfo.total"
        layout="total, sizes, prev, pager, next"
        @size-change="onSizeChange"
        @current-change="onPageIndexChange"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="logDetailVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchFormData: {
        routeId: '',
        serviceId: '',
        pageIndex: 1,
        pageSize: 20
      },
      pageInfo: {
        list: [],
        total: 0
      },
      logDetailVisible: false,
      errorMsgFormData: {
        routeId: '',
        instanceId: '',
        pageIndex: 1,
        pageSize: 5
      },
      errorMsgData: {
        title: '',
        name: '',
        version: '',
        pageInfo: {
          rows: [],
          total: 0
        }
      }
    }
  },
  created() {
    this.loadTable()
  },
  methods: {
    loadTable: function() {
      this.post('monitornew.data.page', this.searchFormData, function(resp) {
        this.pageInfo = resp.data
      })
    },
    loadErrorData: function() {
      this.post('monitornew.error.page', this.errorMsgFormData, function(resp) {
        this.errorMsgData.pageInfo = resp.data
        this.logDetailVisible = true
      })
    },
    loadInstanceMonitorInfo(row, treeNode, resolve) {
      this.post('monitornew.routeid.data.get', { routeId: row.routeId }, resp => {
        const children = resp.data
        row.children = children
        resolve(children)
      })
    },
    onShowErrorDetail: function(row) {
      this.errorMsgData.title = `错误日志 ${row.name}（${row.version}）`
      this.errorMsgData.name = row.name
      this.errorMsgData.version = row.version
      this.errorMsgFormData.routeId = row.routeId
      this.errorMsgFormData.instanceId = row.instanceId
      this.loadErrorData()
    },
    onSolve: function(row) {
      this.confirm('确认标记为已解决吗？', function(done) {
        this.post('monitornew.error.solve', { routeId: row.routeId, errorId: row.errorId }, function(resp) {
          done()
          this.loadErrorData()
        })
      })
    },
    onCloseErrorDlg: function() {
      this.loadTable()
    },
    onSizeChange: function(size) {
      this.searchFormData.pageSize = size
      this.loadTable()
    },
    onAdd: function() {
      this.dialogTitle = '新增IP'
      this.dialogVisible = true
      this.dialogFormData.id = 0
    },
    onPageIndexChange: function(pageIndex) {
      this.searchFormData.pageIndex = pageIndex
      this.loadTable()
    }
  }
}
</script>
