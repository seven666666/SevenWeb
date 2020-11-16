import { saveAs } from 'file-saver';
import { validatenull } from '@/assets/common/js/util.js'
import { commonDateFormat } from '@/assets/common/js/dateformat.js'

export function exportTxt(info, fileName) {
  var FileSaver = require('file-saver');
  var blob = new Blob([info], { type: "text/plain;charset=utf-8" });
  FileSaver.saveAs(blob, fileName + ".txt");
}


//header,导出excel的表头信息
//data，导出excel的数据信息
//filtervalue，过滤需要导出的data中的字段名
//filename,导出的文件名
export function exportExcelJson(header, list, filtervalue, filename) {
  require.ensure([], () => {
    const {
      export_json_to_excel
    } = require('@/assets/common/js/Export2Excel.js');
    //筛选打印数据
    let data = []
    if (validatenull(filtervalue)) {
      data = list
    } else {
      data = formatJson(filtervalue, list);
    }
    //filename加处理
    let datestr = commonDateFormat(new Date())
    filename = filename + '_' + datestr
    export_json_to_excel({
      header: header,
      data,
      filename: filename,
      autoWidth: true,
      bookType: 'xlsx'
    })
  })
}

function formatJson(filterVal, jsonData) {
  return jsonData.map(v => filterVal.map(j => v[j]))
}
