import {constantRouterMap} from '@/router/index'
/* layout */
import Layout from '@/components/layout/Layout'
const _import = require('@/router/_import_' + process.env.NODE_ENV)
/**
 * 判断用户是否拥有此菜单
 * @param permissionList
 * @param menu
 */
function hasPermission(permissions, menu) {
  /*if (menu.menu) {
    /!*
    * 如果这个路由有menu属性,就需要判断用户是否拥有此menu权限
    *!/
    return permissions.indexOf(menu.menu) > -1;
  } else {
    return true
  }*/
  for (let index in permissions) {
    if(permissions[index].split(':').indexOf(menu.code) > -1){
      return true;
    }
  }
  return false;
}


/**
 * 过滤用户授权菜单，如果顶级菜单都没权限，下面的也不显示了
 * @param menuList
 * @param permissions
 */
function filterAvaliableMenus(menuList, permissions) {
  const accessedRouters = menuList.filter(menu => {
    //filter,js语法里数组的过滤筛选方法
    if (hasPermission(permissions, menu)) {
      if (menu.children && menu.children.length) {
        //如果这个路由下面还有下一级的话,就递归调用
        menu.children = filterAvaliableMenus(menu.children, permissions);
        //如果过滤一圈后,没有子元素了,这个父级菜单就也不显示了
        return (menu.children && menu.children.length)
      }
      return true
    }
    return false
  })
  return accessedRouters
}

/**
 * 构建左侧导航树
 * @param menuList
 * @returns {*}
 */
function buildMenuTree(menuList) {
  for(let i in menuList) {
    let menu = menuList[i];
    menu.code = menu.mCode;
    menu.redirect = menu.mUrl;
    menu.name = menu.mName;
    menu.meta = {};
    menu.meta.title=menu.mName;
    menu.meta.icon=menu.mIcon;
    if(!menu.parentId) {
      menu.path = '/'+menu.mCode;
      menu.component = Layout;
      if(menu.children.length > 0){
        buildMenuTree(menu.children);
      }
    }else{
      menu.path = menu.mCode;
      menu.component = _import(menu.redirect);
      delete menu['redirect'] // 删掉子菜单的redirect属性 否则子菜单的页面会跳转到指定redirect上
    }
  }
  return menuList.concat({path: '*', redirect: '/404', hidden: true});

}

const permission = {
  state: {
    routers: constantRouterMap, //本用户所有的路由,包括了固定的路由和下面的addRouters
    addRouters: [] //本用户的角色赋予的新增的动态路由
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)//将固定路由和新增路由进行合并, 成为本用户最终的全部路由信息
    }
  },
  actions: {
    GenerateRoutes({commit}, {menuList,permissions}) {
      //生成路由
      return new Promise(resolve => {
        //roles是后台传过来的角色数组,比如['管理员','文章']
        /*const role = roleInfo.roleName;
        const menus = userPermission.menuList;
        //声明 该角色可用的路由
        let accessedRouters
        if (role === '管理员') {
          //如果角色里包含'管理员',那么所有的路由都可以用
          //其实管理员也拥有全部菜单,这里主要是利用角色判断,节省加载时间
          accessedRouters = menuList
        } else {
          //否则需要通过以下方法来筛选出本角色可用的路由
          accessedRouters = filterAsyncRouter(menuList, menus)
        }*/
        // accessedRouters = buildMenuTree(menuList);
        //执行设置路由的方法
        menuList = buildMenuTree(menuList);
        // console.log(JSON.stringify(menuList));
        // 再根据用户权限过滤一次菜单显示
        commit('SET_ROUTERS', filterAvaliableMenus(menuList, permissions));
        resolve()
      })
    }
  }
}
export default permission
