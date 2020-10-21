<template>
  <avue-data-cardtext :option="option"></avue-data-cardtext>
</template>

<script>
export default {
  name: 'test',
  data() {
    let that = this
    return {
      option: {
        span: 8,
        data: [
          {
            click: function (item) {
              that.gotoTableEditTest()
            },
            title: 'avue-表格编辑',
            color: 'grey',
            content:
              'avue-表格直接更改测试:\n' +
              '1.data中的$cellEdit控制第一次打开表格，改行数据是否可编辑\n' +
              '2.cellBtn设置为true则开启行编辑按钮，在配置中将编辑的字段设置cell为true',
          },
        ],
      },
    }
  },
  created() {},
  methods: {
    /**
     * 表格直接更改测试
     */
    gotoTableEditTest() {
      this.$emit('gotoChildPage', 'test_tableEdit')
    },
  },
}
</script>

<style>
.avue-data-cardText .item-content {
  white-space: pre-wrap;
  text-align: left;
}
</style>
