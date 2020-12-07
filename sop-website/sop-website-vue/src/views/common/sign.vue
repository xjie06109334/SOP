<template>
  <div class="app-container">
    <mavon-editor
      v-model="content"
      :boxShadow="false"
      :subfield="false"
      defaultOpen="preview"
      :editable="false"
      :toolbarsFlag="false"
    />
  </div>
</template>
<script>
import { mavonEditor } from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

export default {
  components: { mavonEditor },
  data() {
    return {
      content: ''
    }
  },
  created() {
    this.getFile('static/doc/common/sign.md', (content) => {
      this.content = content
    })
  }
}
</script>
