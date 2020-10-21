import { axios } from '@/main'

/**
 * 以post形式访问后台接口
 *
 * @param {接口名称} url
 * @param {map格式的参数，对应后台每一个参数} params
 */
export function postRequest(url, params) {
  let param = initURLSearchParams(params)
  return new Promise((resolve, reject) => {
    axios({
      url: url,
      method: "post",
      params: param
    }).then(res => {
      resolve(res)
    }).catch(reason => {
      // 网络异常等情况没有response信息
      let exmsg
      if (reason.response !== undefined) {
        exmsg = '调用服务端异常：' + reason.response.data
        window.Vue.prototype.$alert(exmsg, '错误', {
          confirmButtonText: '确定',
        })
      } else {
        exmsg = '请检查如下项目【1.网络是否正常 2.连接配置文件是否正确 3.服务端是否启动】'
        window.Vue.prototype.$alert(exmsg, '错误', {
          confirmButtonText: '确定',
        })
      }
      reject(exmsg)
    });
  })
}

function initURLSearchParams(params) {
  let param = new URLSearchParams();
  if (!params) {
    return param
  }
  Object.keys(params).forEach(function (key) {
    param.append(key, params[key])
  });
  return param
}
