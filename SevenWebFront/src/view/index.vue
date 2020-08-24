<template>
  <el-container>
    <el-header class="index-head">
      <div class="index-head-div">Seven Web</div>
    </el-header>
    <el-main>
      <el-row :gutter="20">
        <el-col :span="6"
          ><el-button type="primary" round plain @click="gotoStudyCell"
            >学习工作表格</el-button
          ></el-col
        >
        <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
        <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
        <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
      </el-row>
    </el-main>
  </el-container>
</template>
<style scoped></style>
<script>
import "@/assets/common/css/indexCss.css";

export default {
  name: "index",
  data() {
    return {};
  },
  methods: {
    gotoStudyCell() {
      console.log("gotoStudyCell");
      this.$router.push("/studyCell");
    }
  }
};
</script>
