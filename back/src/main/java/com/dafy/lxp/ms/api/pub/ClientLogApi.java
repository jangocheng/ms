package com.dafy.lxp.ms.api.pub;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.service.pub.IClientLogService;
import com.dafy.yihui.common.po.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 客户端日志控制器
 * Created by liaoxudong
 * Date:2018/3/5
 */
@RestController
@RequestMapping("/public/clientLog")
public class ClientLogApi {
    @Autowired
    private IClientLogService clientLogService;
    @GetMapping("/list")
    public Response list(@RequestHeader Map<String,Object> request, @RequestParam("phoneNo") String phoneNo, @RequestParam("date") String date){
        Request parse = Request.parse(request);
        parse.put("phoneNo", phoneNo);
        parse.put("date", date);
        return clientLogService.list(parse);
    }
}
