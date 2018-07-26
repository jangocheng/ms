package com.dafy.lxp.ms.service.system;

import com.dafy.lxp.ms.TestBaseConfig;
import com.dafy.yihui.common.po.Response;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/2/5
 */

public class RoleServiceTest extends TestBaseConfig{

    @Autowired
    private IRoleService roleService;

    @Test
    public void findRoleInfos(){
        Map<String,Integer> header = new HashMap<>();
        header.put("pagenum", 1);
        header.put("pagerow", 50);
        Response list = roleService.list(header);
        System.out.println(list);
    }
}
