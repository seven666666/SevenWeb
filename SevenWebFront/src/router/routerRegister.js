//自动扫描src/view 目录下的文件并注册路由
const pageRouter = [];
const componentsContext = require.context('../view/', true, /\.vue$/);
componentsContext.keys().forEach(component => {
  const componentConfig = componentsContext(component);
  const ctrl = componentConfig.default || componentConfig;
  const routerName = ctrl.name;
  routerName && pageRouter.push({
    path: `/${routerName}`,
    title: routerName,//这里我们可以用对象的key和value来展示汉字
    name: routerName,
    component: () =>
      import(`@/view/${routerName}.vue`)
  });
});
export default pageRouter
