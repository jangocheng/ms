import Vue from 'vue'
import 'normalize.css/normalize.css'// A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN'
import App from './App'
import router from './router'
import store from './store'
import './icons' // icon
import {default as api} from './utils/api'
import {hasPermission} from "./utils/permission";
import SvgIcon from '@/components/SvgIcon';// 图标svg组件
//
// 注册图标 register globally
Vue.component('svg-icon', SvgIcon);
Vue.use(ElementUI, {locale});
//全局的常量
Vue.prototype.api = api;
Vue.prototype.hasPerm = hasPermission;
//生产环境时自动设置为 false 以阻止 vue 在启动时生成生产提示。
Vue.config.productionTip = (process.env.NODE_ENV != 'production')
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: {App}
})

