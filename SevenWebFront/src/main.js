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
