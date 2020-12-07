<template>
  <el-table
    :data="data"
    border
    row-key="id"
    default-expand-all
    :tree-props="{ children: 'refs', hasChildren: 'hasChildren' }"
    :cell-style="cellStyleSmall()"
    :header-cell-style="headCellStyleSmall()"
    empty-text="无参数"
  >
    <el-table-column
      prop="name"
      label="名称"
      width="250"
    />
    <el-table-column
      prop="type"
      label="类型"
      width="100"
    >
      <template slot-scope="scope">
        <span>{{ scope.row.type }}</span>
        <span v-show="scope.row.type === 'array' && scope.row.elementType">
          <el-tooltip effect="dark" :content="`元素类型：${scope.row.elementType}`" placement="top">
            <i class="el-icon-info"></i>
          </el-tooltip>
        </span>
      </template>
    </el-table-column>
    <el-table-column
      prop="required"
      label="必须"
      width="60"
    >
      <template slot-scope="scope">
        <span :class="scope.row.required ? 'danger' : ''">{{ scope.row.required ? '是' : '否' }}</span>
      </template>
    </el-table-column>
    <el-table-column
      prop="maxLength"
      label="最大长度"
    />
    <el-table-column
      prop="description"
      label="描述"
    />
    <el-table-column
      prop="paramExample"
      label="示例值"
    >
      <template slot-scope="scope">
        <div v-if="scope.row.type === 'enum'">
          {{ (scope.row.enums || []).join('、') }}
        </div>
        <div v-else>
          {{ scope.row.paramExample }}
        </div>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: 'ParameterTable',
  props: {
    data: {
      type: Array,
      default: () => []
    },
    tree: {
      type: Boolean,
      default: true
    }
  }
}
</script>
