import Vue from "vue";
import Router from "vue-router";
import index from "@/view/index";
import pageRouter from './routerRegister';

Vue.use(Router);
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}


export default new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      name: "index2",
      component: index,
      children: [
        // 需要显示的页面需要放置在view根目录下才能识别
        ...pageRouter,
      ]
    },
  ]
});
