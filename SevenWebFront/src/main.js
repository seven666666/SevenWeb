import Vue from "vue";
import App from "./App";
import router from "./router";
import "vant/lib/index.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.min";
import ElementUI from "element-ui";
import VueI18n from "vue-i18n";
import "element-ui/lib/theme-chalk/index.css";
import VueResource from "vue-resource";
import Vuex from "vuex";
import Avue from '@smallwei/avue';
import '@smallwei/avue/lib/index.css';
import '@/assets/common/js/dateformat.js';

/*---------使用AXIOS-----------*/
// 引用axios，并设置基础URL为nginx监听url
export var axios = require("axios");
axios.defaults.baseURL = "http://127.0.0.1:8099";
axios.defaults.headers.post["Content-Type"] = "application/json;charset=utf-8";
// 将API方法绑定到全局
Vue.prototype.$axios = axios;

/*---------其它-----------*/
Vue.use(VueResource);
Vue.use(Vuex);
Vue.config.productionTip = false;

/*---------页面框架-----------*/
Vue.use(ElementUI);
Vue.use(Avue);

/*---------使用语言包-----------*/
Vue.use(VueI18n);
const i18n = new VueI18n({
  locale: "CN", // 语言标识
  messages: {
    CN: require("./assets/common/lang/cn"), // 中文语言包
    EN: require("./assets/common/lang/en") // 英文语言包
  }
});

/* eslint-disable no-new */
new Vue({
  el: "#app",
  router,
  i18n,
  render: h => h(App),
  components: { App },
  template: "<App/>"
});

window.Vue = Vue

/*---------自定义拖拽方法-----------*/
Vue.directive('tagDrag', {
  bind(el, binding, vnode, oldVnode) {
    // 获取拖拽内容
    const elTag = el;
    elTag.style.cursor = 'move';

    // 获取原有属性 ie dom元素.currentStyle 火狐谷歌 window.getComputedStyle(dom元素, null);
    const sty = elTag.currentStyle || window.getComputedStyle(elTag, null);

    // 鼠标按下事件
    elTag.onmousedown = (e) => {
      // 鼠标按下，计算当前元素距离可视区的距离 (鼠标点击位置距离可视窗口的距离)
      const disX = e.clientX - elTag.offsetLeft;
      const disY = e.clientY - elTag.offsetTop;

      // 获取到的值带px 正则匹配替换
      let styL, styT;

      // 获取节点距离浏览器视口的高度
      var top = e.currentTarget.getBoundingClientRect().top;
      // 获取节点距离浏览器视口的宽度
      var left = e.currentTarget.getBoundingClientRect().left;
      styL = +left;
      styT = +top;

      // 鼠标拖拽事件
      document.onmousemove = function (e) {
        // 通过事件委托，计算移动的距离 （开始拖拽至结束拖拽的距离）
        const l = e.clientX - disX;
        const t = e.clientY - disY;

        let finallyL = l + styL
        let finallyT = t + styT
        console.log("onmousemove finallyL:finallyT" + finallyL + ":" + finallyT)
        // 边界值判定 注意clientWidth scrollWidth区别 要减去之前的top left值
        if (finallyL < 0) {
          finallyL = 0
        } else if (finallyL > elTag.offsetParent.clientWidth - elTag.clientWidth) {
          finallyL = elTag.offsetParent.clientWidth - elTag.clientWidth
        }

        if (finallyT < 0) {
          finallyT = 0
        } else if (finallyT > elTag.offsetParent.clientHeight - elTag.clientHeight) (
          finallyT = elTag.offsetParent.clientHeight - elTag.clientHeight
        )

        // 移动当前元素
        elTag.style.left = `${finallyL}px`;
        elTag.style.top = `${finallyT}px`;

        //将此时的位置传出去
        //binding.value({x:e.pageX,y:e.pageY})
      };

      document.onmouseup = function (e) {
        document.onmousemove = null;
        document.onmouseup = null;
      };
    }
  }
})
