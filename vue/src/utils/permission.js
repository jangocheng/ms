import store from '../store'

/**
 * 用于全局检查
 * @param permission
 * @returns {boolean}
 */
export function hasPermission(permission) {
  let myPermissions = store.getters.permissionList;
  myPermissions = toPermissions(myPermissions)
  return myPermissions.indexOf(permission) > -1;
}

export function toPermissions(permissionList) {
  let permissions = [];
  permissionList.forEach(p => {
    permissions.push(p.pCode);

  });
  return permissions;
}

