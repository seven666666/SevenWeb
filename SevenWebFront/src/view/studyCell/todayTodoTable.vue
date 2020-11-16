<template>
  <div>
    <avue-crud :data="data" :option="option0" :page.sync="todayPage" @current-change="todayTodoChange" @row-del="todayTodoDelBtn" @row-update='todayTodoUpdate' @refresh-change='getTodayTodoTable'>
      <template slot="menuLeft">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="centerDialogVisible = true">
        </el-button>
        <el-button type="primary" size="small" icon="el-icon-download" @click="downloadTodayTxt">
        </el-button>
      </template>
      <template slot="finish" slot-scope="scope">
        <yesOrNoIcon :judge='scope.row.finish == 1'></yesOrNoIcon>
      </template>
    </avue-crud>
    <!-- 添加弹出框 -->
    <el-dialog :title="$t('todaytodoTable.addBtn')" :visible.sync="centerDialogVisible" width="50%" center>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="从任务列表选择" name="first">
          <avue-form v-model="addBtnModel" :option="addButtonOption" @submit="addTodaySure">
          </avue-form>
        </el-tab-pane>
        <el-tab-pane label="新增任务计划" name="second">
          <avue-form v-model="addBtnNewPlanModel" :option="addButtonNewPlanOption" @submit="addTodayNewPlanSure">
          </avue-form>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import { postRequest } from '@/api/request'
import yesOrNoIcon from '@/view/common/yesOrNoIcon'
import { getPagedData } from '@/assets/common/js/util.js'
import { exportTxt } from '@/assets/common/js/saveFile.js'
import axios from 'axios'

export default {
  data() {
    return {
      // 今日待完成内容
      data: [],
      option0: {
        align: 'center',
        menuAlign: 'left', // 操作栏的位置
        menuType: 'icon', // 操作栏的按钮显示
        menuWidth: '120', // 操作栏的宽度
        stripe: true, // 条纹
        addBtn: false, // 添加按钮隐藏
        delBtn: false, // 删除按钮隐藏
        column: [
          {
            label: '项目',
            prop: 'project',
            minWidth: '220',
            overHidden: true,
            editDisabled: true,
          },
          {
            label: '百分比',
            prop: 'percent',
            minWidth: '120',
            append: '%',
          },
          {
            label: '完成情况',
            prop: 'finish',
            slot: true,
            type: 'select',
            dicData: [
              { label: '已完成', value: 1 },
              { label: '未完成', value: 0 },
            ],
          },
          {
            label: '今日任务备注',
            prop: 'dailyRemarks',
            overHidden: true,
          },
        ],
      },
      todayPage: {
        pageSize: 4,
        currentPage: 1, // 当前页数
        layout: 'total, pager, prev, next',
      },
      totalData: [],
      // 添加按钮弹出框显隐
      centerDialogVisible: false,
      // 添加每日任务增加计划进度
      addButtonOption: {
        column: [
          {
            type: 'select',
            label: '项目名称',
            cascaderItem: [],
            span: 24,
            display: true,
            props: {
              label: 'label',
              value: 'value',
            },
            prop: 'project_name',
            dicData: [],
            clearable: true,
            filterable: true,
            multiple: false,
            required: true,
            rules: [
              {
                required: true,
                message: '请选择项目名称',
              },
            ],
          },
          {
            type: 'input',
            label: '当前任务进度',
            span: 24,
            display: true,
            prop: 'now_percent',
            disabled: true,
            placeholder: '请选择项目名称',
          },
          {
            type: 'input',
            label: '百分比',
            span: 24,
            display: true,
            prop: 'percent',
            required: true,
            rules: [
              {
                required: true,
                message: '百分比必须填写',
              },
              {
                validator: (rule, value, callback) => {
                  if (value.search('^100$|^(\\d|[1-9]\\d)$') < 0) {
                    callback(new Error('必须为小于100的数字'))
                  } else {
                    callback()
                  }
                },
                trigger: 'blur',
              },
            ],
            append: '%',
            pattern: '^100$|^(\\d|[1-9]\\d)$',
          },
          {
            label: '今日任务备注',
            prop: 'dailyRemarks',
            span: 24,
          },
          {
            label: '当前是否已完成',
            prop: 'flag',
            type: 'switch',
            display: true,
            value: 0,
            dicData: [
              {
                label: '',
                value: '0',
              },
              {
                label: '',
                value: '1',
              },
            ],
          },
        ],
        labelPosition: 'left',
        labelSuffix: '：',
        labelWidth: 140,
        gutter: 0,
        menuBtn: true,
        submitBtn: true,
        submitText: '提交',
        emptyBtn: false,
        menuPosition: 'center',
      },
      addBtnModel: {},
      // 添加每日任务增加任务计划 todo
      addBtnNewPlanModel: {},
      addButtonNewPlanOption: {
        column: [
          {
            type: 'input',
            label: '项目名称',
            span: 24,
            display: true,
            prop: 'project_name',
            required: true,
            rules: [
              {
                required: true,
                message: '项目名称必须填写',
              },
            ],
          },
          {
            type: 'input',
            label: '百分比',
            span: 24,
            display: true,
            prop: 'percent',
            required: true,
            rules: [
              {
                required: true,
                message: '百分比必须填写',
              },
              {
                validator: (rule, value, callback) => {
                  if (value.search('^100$|^(\\d|[1-9]\\d)$') < 0) {
                    callback(new Error('必须为小于100的数字'))
                  } else {
                    callback()
                  }
                },
                trigger: 'blur',
              },
            ],
            append: '%',
            pattern: '^100$|^(\\d|[1-9]\\d)$',
          },
          {
            label: '优先级',
            prop: 'priority',
            type: 'select',
            span: 24,
            rules: [
              {
                required: true,
                message: '请选择优先级',
              },
            ],
            dicData: [
              { label: 1, value: 1 },
              { label: 2, value: 2 },
              { label: 3, value: 3 },
              { label: 4, value: 4 },
              { label: 5, value: 5 },
            ],
          },
          {
            label: '今日任务备注',
            prop: 'dailyRemarks',
            span: 24,
          },
          {
            label: '计划备注',
            prop: 'remarks',
            span: 24,
          },
          {
            label: '当前是否已完成',
            prop: 'flag',
            type: 'switch',
            display: true,
            value: 0,
            dicData: [
              {
                label: '',
                value: '0',
              },
              {
                label: '',
                value: '1',
              },
            ],
          },
        ],
        labelPosition: 'left',
        labelSuffix: '：',
        labelWidth: 140,
        gutter: 0,
        menuBtn: true,
        submitBtn: true,
        submitText: '提交',
        emptyBtn: false,
        menuPosition: 'center',
      },
      activeName: 'first', // 添加按钮tabmodel
    }
  },
  components: {
    yesOrNoIcon,
  },
  watch: {
    addBtnModel: {
      handler(val) {
        const dicData = this.addButtonOption.column[0].dicData
        const dicDataOne = dicData.filter((i) => i.value == this.addBtnModel.project_name)
        this.addBtnModel.now_percent = dicDataOne[0].percent
      },
      deep: true,
    },
  },
  methods: {
    getPagedData,
    // 获取今日待完成
    getTodayTodoTable() {
      postRequest('studyCell/getTodayTodoTable', null).then((res) => {
        console.log('getTodayTodoTable' + JSON.stringify(res.data))
        this.totalData = res.data
        this.data = this.getPagedData(res.data, this.todayPage.currentPage, this.todayPage.pageSize)
        this.todayPage.total = this.totalData.length
      })
    },
    todayTodoChange() {
      this.data = this.getPagedData(
        this.totalData,
        this.todayPage.currentPage,
        this.todayPage.pageSize
      )
    },
    // 添加今日需完成的任务
    addTodaySure(form, done) {
      this.centerDialogVisible = false
      postRequest('studyCell/addTodayPlan', {
        project_name: this.addBtnModel.project_name,
        percent: this.addBtnModel.percent,
        flag: this.addBtnModel.flag,
        dailyRemarks: this.addBtnModel.dailyRemarks,
      })
        .then((res) => {
          console.log('addTodayPlan:' + JSON.stringify(res.data))
          if (res.data != 'success') {
            this.$message.error({
              title: 'error',
              message: '添加失败，请联系管理员',
            })
          } else {
            this.$message.success({
              title: 'success',
              message: '添加成功',
            })

            if (this.addBtnModel.flag == 1) {
              // 直接完成当前任务，需更新任务列表
              this.$emit('refreshStudyAndPlan')
            }
            this.addBtnModel = {}
          }
          done()
          this.getTodayTodoTable()
        })
        .catch((reason) => {
          done(false)
        })
    },
    /**
     * 删除按钮 暂不使用
     */
    todayTodoDelBtn: function (row, index) {
      console.log('todayTodoDelBtn,row:' + JSON.stringify(row))
      this.$alert('是否确认删除 [' + row.project + '] 今日待办项目', '提示', {
        confirmButtonText: '确认',
        callback: (action) => {
          if (action == 'confirm') {
            console.log('todayTodoDelBtn confirm')
          }
        },
      })
    },
    // 初始化下拉列表
    refreshSelect() {
      postRequest('studyCell/getPlanDicData', null).then((res) => {
        console.log('getPlanDicData' + JSON.stringify(res.data))
        this.addButtonOption.column[0].dicData = res.data
      })
    },
    // 编辑近日任务状态
    todayTodoUpdate: function (row, index, done) {
      console.log('row' + JSON.stringify(row))
      postRequest('studyCell/updateTodayTodo', row).then((res) => {
        console.log('updateTodayTodo:' + JSON.stringify(res.data))
        if (res.data == 'failed') {
          this.$message.error({
            title: 'error',
            message: '更新失败，请联系管理员',
          })
        } else if (res.data == 'warning') {
          this.$message.warning({
            title: 'warning',
            message: '进度添加后在0-100范围外 请检查',
          })
        } else {
          this.$message.success({
            title: 'success',
            message: '更新成功',
          })
        }
        done()
        // 刷新
        this.getTodayTodoTable()
        this.$emit('refreshStudyAndPlan')
      })
    },
    /**
     * 添加按钮内tab也切换触发
     */
    handleClick(tab, event) {
      console.log(tab, event)
    },
    /**
     * 通过每日任务添加任务计划
     */
    addTodayNewPlanSure(form, done) {
      this.centerDialogVisible = false
      postRequest('studyCell/addTodayPlanAndPlan', this.addBtnNewPlanModel)
        .then((res) => {
          console.log('addTodayPlanAndPlan:' + JSON.stringify(res.data))
          if (res.data.indexOf('failed') > -1) {
            this.$message.error({
              title: 'error',
              message: res.data,
            })
          } else {
            this.$message.success({
              title: 'success',
              message: '添加成功',
            })
            this.addBtnNewPlanModel = {}
            // 需更新任务列表
            this.$emit('refreshStudyAndPlan')
          }
          done()
          this.getTodayTodoTable()
        })
        .catch((reason) => {
          done(false)
        })
    },
    /**
     * 下载每日完成事务
     */
    downloadTodayTxt() {
      console.log('this.data', this.data)
      let datatxt = ''
      for (let index in this.data) {
        let i = this.data[index]
        datatxt += index + '. ' + i.project + '-' + i.percent + '%-' + i.$finish + '\r\n'
      }
      let today = new Date()
      exportTxt(datatxt, 'todayTodo_' + today.format('yyyyMMdd'))
    },
  },
  created() {
    // 获取今日待完成
    this.getTodayTodoTable()
    // 初始化下拉列表
    this.refreshSelect()
  },
}
</script>

<style>
</style>
