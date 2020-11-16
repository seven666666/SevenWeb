<template>
  <div>
    <avue-crud :data="dataTotalSchedual" :option="optionTotalSchedual" :page.sync="planPage" @current-change="planTableChange" @row-save="planHandleSave" @row-del="planDelBtn" @row-update='planUpdate' @date-change="dateChange">
      <template slot="menuLeft">
        <el-button type="primary" size="small" icon="el-icon-download" @click="downloadPlanExcel">
        </el-button>
      </template>
      <template slot="percent" slot-scope="scope">
        <el-progress :percentage="Number.parseInt(scope.row.percent)?Number.parseInt(scope.row.percent):0"></el-progress>
      </template>
    </avue-crud>
  </div>
</template>

<script>
import { postRequest } from '@/api/request'
import { getPagedData, validatenull, changeMapToList } from '@/assets/common/js/util.js'
import { exportExcelJson } from '@/assets/common/js/saveFile.js'

export default {
  data() {
    return {
      // 计划列表
      dataTotalSchedual: [], // 当前页显示的数据
      optionTotalSchedual: {
        align: 'center',
        menuAlign: 'left', // 操作栏的位置
        menuType: 'icon', // 操作栏的按钮显示
        menuWidth: '120', // 操作栏的宽度
        stripe: true, // 条纹
        dateBtn: true,
        column: [
          {
            label: '项目',
            prop: 'project',
            minWidth: '220',
            overHidden: true,
            rules: [
              {
                required: true,
                message: '请选择项目名称',
              },
            ],
          },
          {
            label: '进度',
            prop: 'percent',
            slot: true,
            addDisplay: false,
            sortable: true,
            rules: [
              {
                required: true,
                message: '百分比必须填写',
              },
              {
                validator: (rule, value, callback) => {
                  if (value.toString().search('^100$|^(\\d|[1-9]\\d)$') < 0) {
                    callback(new Error('必须为小于100的数字'))
                  } else {
                    callback()
                  }
                },
                trigger: 'blur',
              },
            ],
          },
          {
            label: '开始时间',
            prop: 'start_time',
            editDisplay: false,
            addDisplay: false,
            type: 'date',
          },
          {
            label: '结束时间',
            prop: 'end_time',
            editDisplay: false,
            addDisplay: false,
            type: 'date',
          },
          {
            label: '阻断信息',
            prop: 'depends_on',
            addDisplay: false,
          },
          {
            label: '状态',
            prop: 'status',
            addDisplay: false,
            type: 'select',
            sortable: true,
            dicData: [
              { label: '未开始', value: 'notStart' },
              { label: '进行中', value: 'doing' },
              { label: '等待中', value: 'depends' },
              { label: '已完成', value: 'end' },
            ],
            filter: true,
            filtersMethod: this.statusFilterMethod,
          },
          {
            label: '优先级',
            prop: 'priority',
            type: 'select',
            sortable: true,
            dicData: [
              { label: 1, value: 1 },
              { label: 2, value: 2 },
              { label: 3, value: 3 },
              { label: 4, value: 4 },
              { label: 5, value: 5 },
            ],
            rules: [
              {
                required: true,
                message: '优先级必须填写',
              },
            ],
          },
          {
            label: '备注',
            prop: 'remarks',
          },
        ],
      },
      planPage: {
        pageSize: 5,
        currentPage: 1, // 当前页数
        layout: 'total, pager, prev, next',
      },
      dataAllTotalSchedual: [], //所有的数据
      dataSelectTotalSchedual: [], // 经过筛选获取的数据
    }
  },
  watch: {
    dataSelectTotalSchedual: {
      handler(val) {
        this.planTableChange()
        this.planPage.total = val.length
      },
      deep: true,
    },
  },
  methods: {
    getPagedData,
    statusFilterMethod: function (value, row, column) {
      return row.status === value
    },

    /**
     * 日期变更
     */
    dateChange(date) {
      if (validatenull(date)) {
        // 清空显示全部
        this.dataSelectTotalSchedual = this.dataAllTotalSchedual
        return
      }
      const startDate = date[0]
      const endDate = date[1]
      this.dataSelectTotalSchedual = this.dataAllTotalSchedual.filter((item) => {
        let dateFlag =
          validatenull(item.start_time) ||
          (startDate <= item.start_time && item.start_time <= endDate)
        return dateFlag
      })
    },
    // 获取计划一栏表
    getPlanTable() {
      postRequest('studyCell/getPlanTable', null).then((res) => {
        this.dataAllTotalSchedual = res.data
        this.dataSelectTotalSchedual = this.dataAllTotalSchedual
      })
    },
    /**
     * 页数切换
     */
    planTableChange() {
      this.dataTotalSchedual = this.getPagedData(
        this.dataSelectTotalSchedual,
        this.planPage.currentPage,
        this.planPage.pageSize
      )
    },
    planHandleSave: function (row, done) {
      postRequest('studyCell/addPlanTable', row)
        .then((res) => {
          console.log('addPlanTable:' + JSON.stringify(res.data))
          if (res.data.indexOf('failed') > -1) {
            this.$message.error({
              title: 'error',
              message: res.data,
            })
          }
          done()
          this.getPlanTable()
          // 告诉父组件需要更新每日更新表的select
          this.$emit('refreshTodayTodoSelect')
        })
        .catch((reason) => {})
    },
    planDelBtn: function (row, index) {
      console.log('planDelBtn,row:' + JSON.stringify(row.id))
      this.$alert('是否确认删除 [' + row.project + '] 计划项目', '提示', {
        confirmButtonText: '确认',
        callback: (action) => {
          if (action == 'confirm') {
            console.log('planDelBtn confirm')
            this.delPlanList(row)
          }
        },
      })
    },
    delPlanList(row) {
      postRequest('studyCell/delPlanList', row)
        .then((res) => {
          if (res.data != 'success') {
            this.$message.error({
              title: 'error',
              message: '删除失败，请联系管理员',
            })
          } else {
            this.$message.success({
              title: 'success',
              message: '删除成功',
            })
            // 告诉父组件需要更新每日更新表的select
            this.$emit('refreshTodayTodoSelect')
          }
          this.getPlanTable()
        })
        .catch((reason) => {})
    },
    // 编辑触发按钮
    planUpdate: function (row, index, done) {
      console.log('row', row)
      // 当日期属性不可编辑时，会以字符串形式传递，删除不需要的日期属性
      delete row.start_time
      delete row.end_time
      postRequest('studyCell/updatePlanTable', row)
        .then((res) => {
          console.log('updatePlanTable:' + JSON.stringify(res.data))
          if (res.data != 'success') {
            this.$message.error({
              title: 'error',
              message: '更新失败，请联系管理员',
            })
          } else {
            this.$message.success({
              title: 'success',
              message: '更新成功',
            })
          }
          done()
          // 刷新
          this.getPlanTable()
          // 告诉父组件需要更新每日更新表的select
          this.$emit('refreshTodayTodoSelect')
        })
        .catch((reason) => {})
    },
    /**
     * 下载excel
     */
    downloadPlanExcel() {
      //头
      const tHeader = changeMapToList(this.optionTotalSchedual.column, 'label')
      //对应的标签
      const filterVal = changeMapToList(this.optionTotalSchedual.column, 'prop')
      //标签对应的内容  是一个数组结构
      const list = this.dataAllTotalSchedual
      exportExcelJson(tHeader, list, filterVal, '计划')
    },
    filterChange(result) {
      this.$message.success('过滤器' + JSON.stringify(result))
    },
  },
  created() {
    // 获取计划一栏表
    this.getPlanTable()
  },
}
</script>

<style>
</style>
