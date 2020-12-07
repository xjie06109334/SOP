<template>
  <div class="app-container">
    <h3>公共错误码</h3>
    <div class="isp-info">
      <el-table
        :data="tableData"
        row-key="id"
        default-expand-all
        border
      >
        <el-table-column
          label="code"
          prop="code"
          width="200"
        />
        <el-table-column
          label="msg"
          prop="msg"
          width="200"
        />
        <el-table-column
          label="sub_code（详细错误码）"
          prop="sub_code"
          width="250"
        />
        <el-table-column
          label="sub_msg（详细错误信息）"
          prop="sub_msg"
        />
        <el-table-column
          label="解决方案"
          prop="solution"
        />
      </el-table>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      tableData: []
    }
  },
  created() {
    console.log(JSON.stringify(this.tableData))
    this.getFile('static/code.json?q=' + new Date().getTime(), json => {
      this.tableData = json
    })
  }
}
</script>
