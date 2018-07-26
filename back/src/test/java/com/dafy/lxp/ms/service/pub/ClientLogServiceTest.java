package com.dafy.lxp.ms.service.pub;

import com.dafy.lxp.ms.TestBaseConfig;
import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.yihui.common.po.Response;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liaoxudong
 * Date:2018/3/5
 */

public class ClientLogServiceTest extends TestBaseConfig{

    @Autowired
    private IClientLogService clientLogService;

    @Test
    public void listTest(){
        Request request = new Request();
        request.put("pageNum", 1);
        request.put("pageRow", 20);
        request.put("phoneNo", "13265719983");
        request.put("date", "2018-01-19");
        Response list = clientLogService.list(request);
        System.out.println(list);
    }
}
