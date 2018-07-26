package com.dafy.lxp.ms.service.system.impl;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.system.Permission;
import com.dafy.lxp.ms.mapper.system.PermissionMapper;
import com.dafy.lxp.ms.service.system.IPermissionService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.lxp.ms.utils.TreeUtils;
import com.dafy.yihui.common.db.po.Page;
import com.dafy.yihui.common.po.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/1/31
 */

@Service
public class PermissionService implements IPermissionService{

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List getUserPermissions(Long id) {
        List<Permission> userPermissions = permissionMapper.getUserPermissions(id);
        /*Map<String, Set<String>> resultMap = new HashMap<>();
        Set<String> pCodes = new HashSet<>();
        resultMap.put("")
        Set<String> pNames = new HashSet<>();
        for (Permission p : userPermissions) {
            pCodes.add(p.getpCode());
            pNames.add(p.getpName());
        }*/
//        List<Permission> menuTrees = TreeUtils.buildTree(userPermissions);
        return userPermissions;
    }

    @Override
    public Response list(Map request) {
        int pageNum = Integer.valueOf(request.get("pagenum").toString());
        int pageRow = Integer.valueOf(request.get("pagerow").toString());
        Page<Permission> page = new Page<>(pageNum, pageRow);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        permissionMapper.findAllPage(map);
        List<Permission> menuList = page.getData();
        page.setData(TreeUtils.buildTree(menuList));
        return CommonUtils.buildSuccessResp(page);
    }

    @Override
    public Response add(Request request) {
        String pCode = request.getString("pCode");
        Long parentId = request.getLong("parentId");
        String pName = request.getString("pName");
        String desc = request.getString("desc");
        Permission permission = new Permission();
        permission.setpCode(pCode);
        permission.setParentId(parentId);
        permission.setpName(pName);
        permission.setDesc(desc);
        permissionMapper.insertSelective(permission);
        return CommonUtils.buildSuccessResp();
    }
}
