package com.dafy.lxp.ms.service.system;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.system.Permission;
import com.dafy.yihui.common.po.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/1/31
 */

public interface IPermissionService {

    List<Permission> getUserPermissions(Long id);

    /**
     * 查询权限列表
     * @param request
     * @return
     */
    Response list(Map request);

    /**
     * 新增权限
     * @param request
     * @return
     */
    Response add(Request request);
}
