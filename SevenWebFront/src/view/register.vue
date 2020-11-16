<template>

  <el-row :gutter="20">
    <el-col :span="12">
      <div class="grid-content bg-purple buildButtonParent">
        <!-- <el-input type="textarea" v-model="desc"> -->
        <div contenteditable=true id="divTest">
          <button contenteditable="false" style="user-modify:read-only;">
            1111&nbsp;
          </button>
          <button contenteditable="false" style="user-modify:read-only;">
            1111
          </button>
        </div>
        <!-- </el-input> -->
        <el-button class="buildButton" size="small" type="primary" plain>生成</el-button>
      </div>
      <div>
        <span>
          正则表达式:
        </span>
        <span>
          result
        </span>
      </div>
    </el-col>
    <el-col :span="12">
      <div>
        <div>
          <span>A:</span>
          <el-tag size="small"> test1 </el-tag>
          <el-tag size="small">test2</el-tag>
          <el-button>...</el-button>
        </div>
        <div>
          <span>B:</span>
          <el-tag size="small"> test3 </el-tag>
          <el-tag size="small">test4</el-tag>
        </div>
        <div>
          <span>B:</span>
          <el-tag type="warning"> test3 </el-tag>
          <el-tag type="warning">test4</el-tag>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'register',
  data() {
    return {
      input1: '',
      desc: '',
    }
  },
}
</script>

<style>
.bg-purple {
  border: 1px solid #acc2da;
  background-color: #e2e4e5;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.buildButtonParent {
  position: relative;
  height: 300px;
}
.buildButton {
  position: absolute;
  right: 0;
  bottom: 0;
}
.el-textarea__inner {
  resize: none;
  height: 260px;
  background-color: rgba(255, 255, 255, 0.4);
}
</style>
