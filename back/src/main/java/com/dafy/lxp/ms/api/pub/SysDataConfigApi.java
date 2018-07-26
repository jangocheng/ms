package com.dafy.lxp.ms.api.pub;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.service.pub.ISysDataConfigService;
import com.dafy.yihui.common.po.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/2/7
 */

@RestController
@RequestMapping("/public/config")
public class SysDataConfigApi {

    @Autowired
    private ISysDataConfigService sysDataConfigService;

    @GetMapping("/list")
    @RequiresPermissions("config:list")
    public Response list(@RequestHeader Map<String,Object> header){
        return sysDataConfigService.list(header);
    }

    @PutMapping("/update")
    @RequiresPermissions("config:update")
    public Response update(@RequestBody Request request){
        return sysDataConfigService.update(request);
    }
}
