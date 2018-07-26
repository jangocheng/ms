package com.dafy.lxp.ms.api.system;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.service.system.IMenuService;
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
@RequestMapping("menu")
public class MenuApi {

    @Autowired
    private IMenuService menuService;

    @RequiresPermissions("menu:list")
    @GetMapping("/list")
    public Response list(@RequestHeader Map header){
        return menuService.list(header);
    }

    @RequiresPermissions("menu:add")
    @PostMapping("/add")
    public Response add(@RequestBody Request request) {
        CommonUtils.hasParams(request,"mCode,mName,mIcon");
        return menuService.add(request);
    }


    @RequiresPermissions("menu:delete")
    @DeleteMapping("/delete/{id}")
    public Response delete(@PathVariable("id") Long id){
        return menuService.delete(id);
    }


    @RequiresPermissions("menu:update")
    @PutMapping("/update")
    public Response update(@RequestBody Request request){
        CommonUtils.hasParams(request,"mCode,mName,mIcon");
        return menuService.update(request);
    }
}
