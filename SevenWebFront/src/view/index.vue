<template>
  <el-container>
    <el-header class="index-head">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="index-head-div" @click="gotoIndex">Seven Web</div>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <asideMenu></asideMenu>
      <el-main>
        <router-view @gotoChildPage="gotoChildPage"></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>
<style scoped>
</style>
<script>
import '@/assets/common/css/indexCss.css'
import asideMenu from './common/asideMenu'

export default {
  name: 'index',
  data() {
    return {}
  },
  components: {
    asideMenu,
  },
  methods: {
    gotoIndex() {
      this.$router.push('mainIndex')
    },
    gotoChildPage(page) {
      this.$router.push(page)
    },
  },
}
</script>
