// 缓存数据
const getters = {
  sidebar: state => state.app.sidebar,
  visitedViews: state => state.app.visitedViews,

  /*userName: state => state.user.username,
  userCode: state => state.user.userCode,
  userPhone: state => state.user.userPhone,
  nickname: state => state.user.nickname,
  userId: state => state.user.userId,*/
  userInfo: state => state.user.userInfo,
  avatar: state => state.user.avatar,
  roleInfo: state => state.user.roleInfo,
  menuTrees: state => state.user.menuTrees,
  permissionList: state => state.user.permissionList,

  permission_routers: state => state.permission.routers,
  addRouters: state => state.permission.addRouters
}
export default getters
