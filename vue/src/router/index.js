import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../components/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
// 基础路径配置
export const constantRouterMap = [
  {path: '/login', component: _import('login/index'), hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '首页',
    hidden: true,
    children: [{
      path: 'dashboard', component: _import('dashboard/index')
    }]
  }
]
const router = new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export default router

import store from '@/store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import {getToken} from '@/utils/cookie' // 验权
const whiteList = ['/login', '/404'] //白名单,不需要登录的路由
// 每个页面请求前置过滤
router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    // 加载左侧菜单
    //如果已经登录
    if (to.path === '/login') {// 登录状态访问/login 跳转到首页
      next({path: '/'})
      NProgress.done() // 结束Progress
    } else if (!store.getters.roleInfo) {
      store.dispatch('GetInfo').then(() => {
        next({...to})
      })
    } else {
      next()
    }
  } else if (whiteList.indexOf(to.path) !== -1) {
    //如果前往的路径是白名单内的,就可以直接前往
    next()
  } else {
    //如果路径不是白名单内的,而且又没有登录,就跳转登录页面
    next('/login')
    NProgress.done() // 结束Progress
  }
})
router.afterEach(() => {
  NProgress.done() // 结束Progress
})

