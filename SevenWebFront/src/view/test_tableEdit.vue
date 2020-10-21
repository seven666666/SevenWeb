<template>
  <div>
    <div>avue-表格直接更改测试</div>
    <avue-crud ref="crud" :option="option" :data="data" @row-update="addUpdate">
    </avue-crud>
  </div>
</template>

<script>
export default {
  name: 'test_tableEdit',
  data() {
    return {
      data: [
        {
          id: 0,
          name: '张三',
          sex: 1,
          $cellEdit: true,
        },
        {
          id: 1,
          name: '李四',
          sex: 0,
        },
      ],
      option: {
        addBtn: false,
        editBtn: false,
        addRowBtn: true,
        cellBtn: true,
        menuWidth: 250,
        column: [
          {
            label: '姓名',
            prop: 'name',
            cell: true,
            rules: [
              {
                required: true,
                message: '请输入姓名',
                trigger: 'blur',
              },
            ],
          },
          {
            label: '性别',
            prop: 'sex',
            type: 'select',
            dicData: [
              {
                label: '男',
                value: 0,
              },
              {
                label: '女',
                value: 1,
              },
            ],
            cell: true,
          },
          {
            label: '开关',
            prop: 'switch',
            type: 'switch',
            cell: true,
          },
        ],
      },
    }
  },
  methods: {
    addUpdate(form, index, done, loading) {
      console.log('编辑数据' + JSON.stringify(form) + '数据序号' + index)
      done()
    },
  },
}
</script>

<style>
</style>
