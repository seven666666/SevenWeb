<template>
  <div>
    <!-- 标识第一个表格及日历的内容 -->
    <el-row :gutter="30" style="margin-bottom:20px">
      <el-col :span="16">
        每日待完成项
      </el-col>
      <el-col :span="8">
        完成情况记录
      </el-col>
    </el-row>
    <!-- 今日待完成项与日历完成情况 -->
    <el-row :gutter="30">
      <el-col :span="16">
        <todayTodoTable ref="todayTodoTableRef" @refreshStudyAndPlan="refreshStudyAndPlan"> </todayTodoTable>
      </el-col>
      <el-col :span="8">
        <ele-calendar :render-content="renderContent" :data="datedef" :prop="prop">
        </ele-calendar>
      </el-col>
    </el-row>
    <hr />
    <!-- 标识第二个表格的内容 -->
    <el-row style="margin-top:30px">
      学习列表一览
    </el-row>
    <!-- 学习列表一览 -->
    <el-row>
      <studyList @refreshTodayTodoTable="refreshTodayTodoTable" ref="studyListRef"></studyList>
    </el-row>
    <hr />
    <!-- 标识第三个表格的内容 -->
    <el-row style="margin-top:30px">
      计划总进度一览
    </el-row>
    <!-- 计划总进度一览 -->
    <el-row>
      <planTable @refreshTodayTodoSelect="refreshTodayTodoSelect" ref="planTableRef"></planTable>
    </el-row>
  </div>
</template>
<style scoped>
.el-date-table-calendar td.current:not(.disabled) {
  color: #fff;
  background-color: #5cadff;
  border-radius: 50%;
}
.el-date-picker-calendar table {
  padding: 10px;
  box-sizing: border-box;
  table-layout: fixed;
  width: 100%;
}
</style>

<script>
import eleCalendar from 'ele-calendar'
import 'ele-calendar/dist/vue-calendar.css'
import { postRequest } from '@/api/request'
// 今日需完成工作
import todayTodoTable from './studyCell/todayTodoTable'
// 学习列表
import studyList from './studyCell/studyList'
// 计划列表
import planTable from './studyCell/planTable'

export default {
  name: 'studyCell',
  data() {
    return {
      // 日历
      datedef: [],
      prop: 'date', //对应日期字段名,
    }
  },
  components: {
    eleCalendar,
    todayTodoTable,
    studyList,
    planTable,
  },
  methods: {
    // 通过render-content的渲染Function 自定义日历显示内容
    renderContent(h, parmas) {
      // 自定义样式,我返回的是小点(橘黄色)
      const loop = (data) => {
        return data.defvalue.value
          ? h('div', [
              h('div', data.defvalue.text),
              h(
                'div',
                {
                  style: {
                    width: '4px',
                    height: '4px',
                    margin: '0 auto',
                    borderRadius: '4px',
                    backgroundColor: 'red',
                  },
                },
                ''
              ),
            ])
          : h('div', [h('div', data.defvalue.text)])
      }
      return h(
        'div',
        {
          // 给div绑定样式
          style: {
            minHeight: '45px',
          },
          click() {},
        },
        [loop(parmas)]
      )
    },
    // 获取日历成果
    getMonthCalendar(date = new Date()) {
      postRequest('studyCell/getMonthCalendar', { date: date })
        .then((res) => {
          this.datedef = res.data
        })
        .catch((reason) => {
          this.$message.error({
            title: 'error',
            message: JSON.stringify(reason),
          })
        })
    },
    /**
     * 更新每日选择,和每日任务显示
     */
    refreshTodayTodoSelect() {
      console.log('refreshTodayTodoSelect')
      this.$refs.todayTodoTableRef.refreshSelect()
      this.$refs.todayTodoTableRef.getTodayTodoTable()
    },
    /**
     * 更新每日待办
     */
    refreshTodayTodoTable() {
      console.log('refreshTodayTodoTable')
      this.$refs.todayTodoTableRef.getTodayTodoTable()
    },
    /**
     * 更新学习及计划表
     */
    refreshStudyAndPlan() {
      console.log('refreshStudyAndPlan')
      this.$refs.studyListRef.getStudyTable()
      this.$refs.planTableRef.getPlanTable()
    },
  },
  created() {
    // 获取日历成果
    this.getMonthCalendar()
  },
}
</script>
