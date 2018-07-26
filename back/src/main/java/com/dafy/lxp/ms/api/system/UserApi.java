package com.dafy.lxp.ms.api.system;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.system.User;
import com.dafy.lxp.ms.service.system.IUserService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.yihui.common.po.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户相关api
 * 登录、注册、权限等
 * Created by liaoxudong
 * Date:2018/1/30
 */

@RestController
@RequestMapping("user")
public class UserApi {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public Response login(@RequestBody Request request) {
        CommonUtils.hasParams(request, "username,password");
        return userService.login(request);
    }

    @GetMapping("/getInfo")
    public Response getInfo() {
        return userService.getInfo();
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping("/logout")
    public Response logout() {
        return userService.logout();
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @RequiresPermissions("user:list")
    @GetMapping("/list/{userId}")
    public Response list(@RequestHeader Map<String, Object> header,@PathVariable("userId") long userId) {
        int currentPage = Integer.parseInt(header.get("pagenum").toString());
        int pageCount = Integer.parseInt(header.get("pagerow").toString());
//        return userService.listUser(currentPage, pageCount);
        return userService.findUserInfos(currentPage, pageCount, userId);
    }

    @RequiresPermissions("role:list")
    @GetMapping("/getAllRoles")
    public Response getAllRoles() {
        return userService.getAllRoles();
    }

    @RequiresPermissions("user:add")
    @PostMapping("/add")
    public Response add(@RequestBody Request request){
        CommonUtils.hasParams(request, "username,phoneNo,roleId,email,password");
        return userService.add(request);
    }

    @RequiresPermissions("user:update")
    @PutMapping("/update")
    public Response update(@RequestBody Request request){
//        CommonUtils.hasParams(request, "username,phoneNo,roleId,email,password");
        return userService.update(request);
    }

    @RequiresPermissions("user:delete")
    @DeleteMapping("/delete")
    public Response delete(@RequestBody User request) {
        return userService.delete(request);
    }
}
