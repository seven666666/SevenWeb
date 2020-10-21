<template>
  <div>
    <avue-crud :data="dataStudyList" :option="optionStudyList" :page.sync="studyPage" @current-change="studyCurrentChange" @row-save="handleSave" @row-del="studyDel">
      <template slot="one_day" slot-scope="scope">
        <yesOrNoIcon :judge='scope.row.one_day'></yesOrNoIcon>
      </template>
      <template slot="two_day" slot-scope="scope">
        <yesOrNoIcon :judge='scope.row.two_day'></yesOrNoIcon>
      </template>
      <template slot="four_day" slot-scope="scope">
        <yesOrNoIcon :judge='scope.row.four_day'></yesOrNoIcon>
      </template>
      <template slot="seven_day" slot-scope="scope">
        <yesOrNoIcon :judge='scope.row.seven_day'></yesOrNoIcon>
      </template>
      <template slot="fifteen_day" slot-scope="scope">
        <yesOrNoIcon :judge='scope.row.fifteen_day'></yesOrNoIcon>
      </template>
    </avue-crud>
  </div>
</template>

<script>
import { postRequest } from '@/api/request'
import yesOrNoIcon from '@/view/common/yesOrNoIcon'
import { getPagedData } from '@/assets/common/js/util.js'

export default {
  data() {
    return {
      // 学习列表
      dataStudyList: [],
      optionStudyList: {
        align: 'center',
        menuAlign: 'left', // 操作栏的位置
        menuType: 'icon', // 操作栏的按钮显示
        menuWidth: '120', // 操作栏的宽度
        stripe: true, // 条纹
        editBtn: false,
        column: [
          {
            label: '项目',
            prop: 'project',
            minWidth: '220',
            overHidden: true,
          },
          {
            label: '开始时间',
            prop: 'start_time',
            type: 'date',
          },
          {
            label: '艾宾浩斯记忆曲线',
            children: [
              {
                label: '1d',
                prop: 'one_day',
                width: 40,
                slot: true,
                addDisplay: false,
              },
              {
                label: '2d',
                prop: 'two_day',
                width: 40,
                slot: true,
                addDisplay: false,
              },
              {
                label: '4d',
                prop: 'four_day',
                width: 40,
                slot: true,
                addDisplay: false,
              },
              {
                label: '7d',
                prop: 'seven_day',
                width: 40,
                slot: true,
                addDisplay: false,
              },
              {
                label: '15d',
                prop: 'fifteen_day',
                width: 40,
                slot: true,
                addDisplay: false,
              },
            ],
          },
        ],
      },
      studyPage: {
        pageSize: 5,
        currentPage: 1, // 当前页数
        layout: 'total, pager, prev, next',
      },
      dataStudyAllList: [],
    }
  },
  components: {
    yesOrNoIcon,
  },
  methods: {
    getPagedData,
    // 获取学习一览表
    getStudyTable() {
      postRequest('studyCell/getStudyTable', null).then((res) => {
        this.dataStudyAllList = res.data
        this.dataStudyList = this.getPagedData(
          res.data,
          this.studyPage.currentPage,
          this.studyPage.pageSize
        )
        this.studyPage.total = res.data.length
      })
    },
    studyCurrentChange() {
      this.dataStudyList = this.getPagedData(
        this.dataStudyAllList,
        this.studyPage.currentPage,
        this.studyPage.pageSize
      )
    },
    /**
     * @title 添加一行数据
     * @param row 为当前的数据
     * @param done 为表单关闭函数
     *
     **/
    handleSave: function (row, done) {
      console.log('row' + JSON.stringify(row))
      console.log(row)
      postRequest('studyCell/addStudyList', row).then((res) => {
        if (res.data != 'success') {
          this.$notify.error({
            title: 'error',
            message: '添加失败，请联系管理员',
          })
        }
        done()
        this.getStudyTable()
        // 告诉父组件需要更新每日更新表
        this.$emit('refreshTodayTodoTable')
      })
    },
    /**
     * 删除项目按钮
     */
    studyDel: function (row, index) {
      console.log('studyDel,row:' + JSON.stringify(row.id))
      this.$alert('是否确认删除 [' + row.project + '] 学习项目', '提示', {
        confirmButtonText: '确认',
        callback: (action) => {
          if (action == 'confirm') {
            this.deleteStudyWork(row)
          }
        },
      })
    },
    deleteStudyWork(row) {
      postRequest('studyCell/delStudyList', row).then((res) => {
        if (res.data != 'success') {
          this.$notify.error({
            title: 'error',
            message: '删除失败，请联系管理员',
          })
        } else {
          this.$notify.success({
            title: 'success',
            message: '删除成功',
          })
          // 告诉父组件需要更新每日更新表
          this.$emit('refreshTodayTodoTable')
        }
        this.getStudyTable()
      })
    },
  },
  created() {
    // 获取学习一览表
    this.getStudyTable()
  },
}
</script>

<style>
</style>
