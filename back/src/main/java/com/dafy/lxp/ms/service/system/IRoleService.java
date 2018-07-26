package com.dafy.lxp.ms.service.system;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.system.Role;
import com.dafy.yihui.common.po.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/1/31
 */

public interface IRoleService {

    Role getRoleByUserId(Long userId);

    List<Role> getAllRoles();

    Response list(Map header);

    /**
     * 添加角色信息
     * @param request
     * @return
     */
    Response add(Request request);

    /**
     * 删除角色
     * @param id
     * @return
     */
    Response delete(Long id);

    /**
     * 更新角色信息
     * @param request
     * @return
     */
    Response update(Request request);
}
