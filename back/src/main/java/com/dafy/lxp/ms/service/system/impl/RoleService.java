package com.dafy.lxp.ms.service.system.impl;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.system.Permission;
import com.dafy.lxp.ms.dto.system.Role;
import com.dafy.lxp.ms.dto.system.RolePermission;
import com.dafy.lxp.ms.mapper.system.PermissionMapper;
import com.dafy.lxp.ms.mapper.system.RoleMapper;
import com.dafy.lxp.ms.mapper.system.RolePermissionMapper;
import com.dafy.lxp.ms.mapper.system.UserRoleMapper;
import com.dafy.lxp.ms.service.system.IRoleService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.lxp.ms.utils.TreeUtils;
import com.dafy.yihui.common.db.po.Page;
import com.dafy.yihui.common.po.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/1/31
 */

@Service
public class RoleService implements IRoleService{
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public Role getRoleByUserId(Long userId) {
        return roleMapper.getRoleByUserId(userId);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleMapper.getAllRoles();
    }

    @Override
    public Response list(Map header) {
        int pageNum = Integer.valueOf(header.get("pagenum").toString());
        int pageRow = Integer.valueOf(header.get("pagerow").toString());
        Page<Role> page = new Page<>(pageNum, pageRow);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        roleMapper.findRoleInfos(map);
        List<Role> roleInfos = page.getData();
        for (Role roleInfo : roleInfos) {
            List<Permission> permissionList = roleInfo.getPermissionList();
            for (Permission p : permissionList) {
                roleInfo.addExistsPermisson(p.getId());
            }
            roleInfo.setPermissionList(TreeUtils.buildTree(permissionList));
        }
        Map<String,Object> result = new HashMap<>();
        result.put("roleInfo", page);
        result.put("permissionList", TreeUtils.buildTree(permissionMapper.findAll()));
        return CommonUtils.buildSuccessResp(result);
    }

    @Override
    public Response add(Request request) {
        Role role = new Role();
        role.setCode(request.getString("code"));
        role.setRoleName(request.getString("roleName"));
        role.setDesc(request.getString("desc"));
        role.setStatus(request.getBoolean("status")?"1":"0");
        roleMapper.insertSelective(role);
        List<RolePermission> rolePermissions = new ArrayList<>();
        List<Integer> pIds = request.get("selectedPermission", List.class);
        for (Integer pId : pIds) {// 批量插入
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId((long)pId);
            rolePermissions.add(rolePermission);
        }
        rolePermissionMapper.inserts(rolePermissions);
        return CommonUtils.buildSuccessResp();
    }

    @Override
    public Response delete(Long id) {
        // 删除角色
        roleMapper.deleteByPrimaryKey(id);
        // 删除用户角色关联
        userRoleMapper.deleteByRoleId(id);
        // 删除角色权限关联
        rolePermissionMapper.deleteByRoleId(id);
        return CommonUtils.buildSuccessResp();
    }

    @Override
    public Response update(Request request) {
        Role role = new Role();
        role.setId(request.getLong("id"));
        role.setCode(request.getString("code"));
        role.setRoleName(request.getString("roleName"));
        role.setDesc(request.getString("desc"));
        role.setStatus(request.getBoolean("status")?"1":"0");
        roleMapper.updateByPrimaryKeySelective(role);
        // 先删除角色权限关联
        rolePermissionMapper.deleteByRoleId(role.getId());
        // 再重新插入
        List<RolePermission> rolePermissions = new ArrayList<>();
        List<Integer> pIds = request.get("selectedPermission", List.class);
        for (Integer pId : pIds) {// 批量插入
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId((long)pId);
            rolePermissions.add(rolePermission);
        }
        rolePermissionMapper.inserts(rolePermissions);
        return CommonUtils.buildSuccessResp();
    }
}
