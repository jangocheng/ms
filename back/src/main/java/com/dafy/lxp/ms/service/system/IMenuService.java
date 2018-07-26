package com.dafy.lxp.ms.service.system;


import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.system.Menu;
import com.dafy.yihui.common.po.Response;

import java.util.List;
import java.util.Map;

/**
 * 菜单相关
 * Created by liaoxudong
 * Date:2018/1/31
 */

public interface IMenuService {

    List<Menu> getUserMenus(Long userId);


    Response list(Map header);

    Response add(Request request);

    Response delete(Long id);

    Response update(Request request);
}
