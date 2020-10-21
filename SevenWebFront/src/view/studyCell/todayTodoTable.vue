<template>
  <div>
    <avue-crud :data="data" :option="option0" :page.sync="todayPage" @current-change="todayTodoChange" @row-del="todayTodoDelBtn" @row-update='todayTodoUpdate'>
      <template slot-scope="scope" slot="menuLeft">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="centerDialogVisible = true">
        </el-button>
      </template>
      <template slot="finish" slot-scope="scope">
        <yesOrNoIcon :judge='scope.row.finish == 1'></yesOrNoIcon>
      </template>
    </avue-crud>
    <!-- 添加弹出框 -->
    <el-dialog title="添加今日计划" :visible.sync="centerDialogVisible" width="30%" center>
      <avue-form v-model="addBtnModel" :option="addButtonOption" @submit="addTodaySure">
      </avue-form>
    </el-dialog>
  </div>
</template>

<script>
import { postRequest } from '@/api/request'
import yesOrNoIcon from '@/view/common/yesOrNoIcon'
import { getPagedData } from '@/assets/common/js/util.js'
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
        column: [
          {
            label: '项目',
            prop: 'project',
            minWidth: '220',
            overHidden: true,
            editDisabled: true,
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
        ],
        labelPosition: 'left',
        labelSuffix: '：',
        labelWidth: 120,
        gutter: 0,
        menuBtn: true,
        submitBtn: true,
        submitText: '提交',
        emptyBtn: false,
        menuPosition: 'center',
      },
      addBtnModel: {},
    }
  },
  components: {
    yesOrNoIcon,
  },
  methods: {
    getPagedData,
    // 获取今日待完成
    getTodayTodoTable() {
      postRequest('studyCell/getTodayTodoTable', null).then((res) => {
        console.log('getTodayTodoTable' + JSON.stringify(res.data))
        this.totalData = res.data
        this.data = this.getPagedData(res.data, this.todayPage.currentPage, this.todayPage.pageSize)
        this.todayPage.total = res.data.length
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
      postRequest('studyCell/addTodayPlan', this.addBtnModel).then((res) => {
        console.log('addTodayPlan:' + JSON.stringify(res.data))
        if (res.data != 'success') {
          this.$notify.error({
            title: 'error',
            message: '添加失败，请联系管理员',
          })
        } else {
          this.addBtnModel = {}
        }
        done()
        this.getTodayTodoTable()
      })
    },
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
      console.log('refreshSelect')
      postRequest('studyCell/getPlanDicData', null).then((res) => {
        console.log('getPlanDicData' + JSON.stringify(res.data))
        this.addButtonOption.column[0].dicData = res.data
      })
    },
    // 编辑近日任务状态
    todayTodoUpdate: function (row, index, done) {
      console.log('row' + JSON.stringify(row))
      console.log(row)
      postRequest('studyCell/updateTodayTodo', row).then((res) => {
        console.log('updateTodayTodo:' + JSON.stringify(res.data))
        if (res.data != 'success') {
          this.$notify.error({
            title: 'error',
            message: '更新失败，请联系管理员',
          })
        } else {
          this.$notify.success({
            title: 'success',
            message: '更新成功',
          })
        }
        done()
        // 刷新
        this.getTodayTodoTable()
      })
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
