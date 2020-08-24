import Vue from "vue";
import Router from "vue-router";
import HelloWorld from "@/components/HelloWorld";
import i18nTest from "@/components/i18nTest";
import index from "@/view/index";
import studyCell from "@/view/studyCell/index";

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {
      path: "/HelloWorld",
      name: "HelloWorld",
      component: HelloWorld
    },
    {
      path: "/i18nTest",
      name: "i18nTest",
      component: i18nTest
    },
    {
      path: "/",
      name: "index",
      component: index
    },

    {
      path: "/studyCell",
      name: "studyCell",
      component: studyCell
    }
  ]
});
