// import {getInfo, login, logout} from '@/api/login'
import {getToken, removeToken, setToken} from '@/utils/cookie'
import {toPermissions} from '@/utils/permission'
import {default as api} from '@/utils/api'
import store from '@/store'
import router from '@/router'

const user = {
  state: {
    userInfo: null,
    avatar: 'http://test-other-file.lixiangpai888.com/495f9ea7ecce4be48e9dc3b85e9691b6.jpg',
    roleInfo: null,
    menuTrees: [],
    permissionList: [],
  },
  mutations: {
    // 全局信息保存
    SET_USER: (state, data) => {
      state.userInfo = data.userInfo;
      state.roleInfo = data.roleInfo;
      state.menuTrees = data.menuTrees;
      state.permissionList = data.permissionList;
    },
    RESET_USER: (state) => {
      state.userInfo = null;
      state.roleInfo = null;
      state.menuTrees = [];
      state.permissionList = [];
    }
  },
  actions: {
    // 登录
    Login({commit, state}, loginForm) {
      return new Promise((resolve, reject) => {
        api({
          url: "/user/login",
          method: "post",
          data: loginForm
        }).then(data => {
          if (data.code === "10") {
            //cookie中保存前端登录状态
            setToken();
          }
          resolve(data);
        }).catch(err => {
          reject(err.desc)
        })
      })
    },
    // 获取用户信息
    GetInfo({commit, state}) {
      return new Promise((resolve, reject) => {
        api({
          url: '/user/getInfo',
          method: 'get'
        }).then(data => {
          //储存用户信息
          commit('SET_USER', data);
          //cookie保存登录状态,仅靠vuex保存的话,页面刷新就会丢失登录状态
          setToken();
          //生成路由
          let permissionList = data.permissionList ;
          // let roleInfo = data.role_info;
          let menuList = data.menuTrees;// || {path: '*', redirect: '/404', hidden: true};
          let permissions = toPermissions(permissionList);
          store.dispatch('GenerateRoutes', {menuList,permissions}).then(() => {
            //生成该用户的新路由json操作完毕之后,调用vue-router的动态新增路由方法,将新路由添加
            router.addRoutes(store.getters.addRouters)
          })
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    LogOut({commit}) {
      return new Promise((resolve) => {
        api({
          url: "/user/logout",
          method: "post"
        }).then(data => {
          commit('RESET_USER')
          removeToken()
          resolve(data);
        }).catch(() => {
          commit('RESET_USER')
          removeToken()
        })
      })
    },
    // 前端 登出
    FedLogOut({commit}) {
      return new Promise(resolve => {
        commit('RESET_USER');
        removeToken();
        resolve()
      })
    }
  }
}
export default user
