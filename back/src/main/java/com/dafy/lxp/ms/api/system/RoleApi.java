package com.dafy.lxp.ms.api.system;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.service.system.IRoleService;
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
@RequestMapping("/role")
public class RoleApi {

    @Autowired
    private IRoleService roleService;

//    @RequiresPermissions("role:list")
    @GetMapping("/list")
    public Response list(@RequestHeader Map header) {
        return roleService.list(header);
    }


    @RequiresPermissions("role:add")
    @PostMapping("/add")
    public Response add(@RequestBody Request request) {
        CommonUtils.hasParams(request,"code,roleName");
        return roleService.add(request);
    }


    @RequiresPermissions("role:delete")
    @DeleteMapping("/delete/{id}")
    public Response delete(@PathVariable("id") Long id){
        return roleService.delete(id);
    }


    @RequiresPermissions("role:update")
    @PutMapping("/update")
    public Response update(@RequestBody Request request){
        CommonUtils.hasParams(request,"code,roleName");
        return roleService.update(request);
    }
}
