<template>
  <div>
    <el-menu class="el-menu-vertical-demo" :collapse="isCollapse">
      <el-menu-item index="1" @click="gotoStudyCell()">
        <i class="el-icon-location"></i>
        <span slot="title">{{$t('index.StudyWorkCell')}}</span>
      </el-menu-item>
      <el-menu-item index="2" @click="gotoTest()">
        <i class="el-icon-menu"></i>
        <span slot="title">{{$t('index.test')}}</span>
      </el-menu-item>
      <el-menu-item index="3" @click="gotoRegis()">
        <i class="el-icon-refrigerator"></i>
        <span slot="title">{{$t('index.register')}}</span>
      </el-menu-item>
    </el-menu>

    <div :class="isCollapse?`el-menu-collapse`:`el-menu-collapse-open`" @click="shutOrOpen"></div>
  </div>
</template>
<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 160px;
  min-height: 60vh;
  /* overflow: hidden; */
}
.el-icon-location {
  margin-right: 10px;
}
/* .el-menu-vertical-demo,
.el-menu--collapse {
  width: 160px;
} */
.el-menu-collapse {
  background-color: #007bff;
  height: 60px;
  width: 20px;
  border-radius: 0 10px 10px 0;
  top: 50vh;
  left: 50px;
  position: fixed;
}
.el-menu-collapse-open {
  background-color: #007bff;
  height: 60px;
  width: 20px;
  top: 50vh;
  left: 140px;
  border-radius: 10px 0 0 10px;
  position: fixed;
}
</style>
<script>
import { gotoStudyCell, gotoTest, gotoRegis } from '@/assets/common/js/page'

export default {
  data() {
    return {
      // 菜单栏是否展开
      isCollapse: true,
    }
  },
  methods: {
    gotoStudyCell,
    gotoTest,
    gotoRegis,
    shutOrOpen() {
      this.isCollapse = !this.isCollapse
    },
  },
}
</script>
