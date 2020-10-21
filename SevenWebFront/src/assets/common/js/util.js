/**
 * 判断是否为空
 */
export function validatenull(val) {
  if (typeof val === 'boolean') {
    return false
  }
  if (typeof val === 'number') {
    return false
  }
  if (val instanceof Array) {
    if (val.length == 0) return true
  } else if (val instanceof Object) {
    if (JSON.stringify(val) === '{}') return true
  } else {
    if (val == 'null' || val == null || val == 'undefined' || val == undefined || val == '' || val.trim() == '') return true
    return false
  }
  return false
}
/**
 * 假分页数据获取
 */
export function getPagedData(data, currentPage, pageSize) {

  if (validatenull(data) || data.length < pageSize) {
    return data
  }
  let pagedData = []
  let startIndex = (currentPage - 1) * pageSize
  let endIndex = Math.min(data.length, currentPage * pageSize)
  for (let i = startIndex; i < endIndex; i++) {
    pagedData.push(data[i])
  }
  return pagedData
}
