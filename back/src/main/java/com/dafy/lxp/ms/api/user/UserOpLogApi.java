package com.dafy.lxp.ms.api.user;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.service.user.IUserOpLogService;
import com.dafy.yihui.common.po.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/4/3
 */

@RestController
@RequestMapping("/user/opLog")
public class UserOpLogApi {
    @Autowired
    private IUserOpLogService userOpLogService;

    @GetMapping("/list")
    public Response list(@RequestHeader Map<String,Object> request, @RequestParam("phoneNo") String phoneNo, @RequestParam("date") String date){
        Request parse = Request.parse(request);
        parse.put("phoneNo", phoneNo);
        parse.put("date", date);
        return userOpLogService.list(parse);
    }
}
