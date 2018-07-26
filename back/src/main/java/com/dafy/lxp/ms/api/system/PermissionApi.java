package com.dafy.lxp.ms.api.system;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.service.system.IPermissionService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.yihui.common.po.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/2/2
 */

@RestController
@RequestMapping("/permission")
public class PermissionApi {
    @Autowired
    private IPermissionService permissionService;

    @RequiresPermissions("permission:list")
    @GetMapping("/list")
    public Response list(@RequestHeader Map request){
        Response list = permissionService.list(request);
        return list;
    }

    @RequiresPermissions("permission:add")
    @PostMapping("/add")
    public Response add(@RequestBody Request request) {
        CommonUtils.hasParams(request,"pCode,pName");
        return permissionService.add(request);

    }
}
