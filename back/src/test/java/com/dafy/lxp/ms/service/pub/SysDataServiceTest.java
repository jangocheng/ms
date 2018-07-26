package com.dafy.lxp.ms.service.pub;

import com.dafy.lxp.ms.TestBaseConfig;
import com.dafy.yihui.common.po.Response;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by liaoxudong
 * Date:2018/2/7
 */

public class SysDataServiceTest extends TestBaseConfig{

    @Autowired
    private ISysDataConfigService sysDataConfigService;
    @Test
    public void test(){
        HashMap<String, Object> header = new HashMap<>();

        Response list = sysDataConfigService.list(header);
        System.out.println(list);
    }
}
