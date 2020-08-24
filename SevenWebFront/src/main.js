import Vue from "vue";
import App from "./App";
import router from "./router";
import $ from "jquery";
import Vant from "vant";
import "vant/lib/index.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.min";
import ElementUI from "element-ui";
import VueI18n from "vue-i18n";
import enLocale from "element-ui/lib/locale/lang/en";
import zhLocale from "element-ui/lib/locale/lang/zh-CN";
import "element-ui/lib/theme-chalk/index.css";
import VueResource from "vue-resource";
import Vuex from "vuex";

// 引用axios，并设置基础URL为nginx监听url
var axios = require("axios");
axios.defaults.baseURL = "http://127.0.0.1:80";
// 将API方法绑定到全局
Vue.prototype.$axios = axios;
Vue.use(VueResource);
Vue.use(Vuex);
Vue.use(ElementUI);
Vue.use(VueI18n);

Vue.config.productionTip = false;

/*---------使用语言包-----------*/
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
