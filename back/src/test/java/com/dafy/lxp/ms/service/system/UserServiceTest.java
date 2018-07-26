package com.dafy.lxp.ms.service.system;

import com.dafy.lxp.ms.TestBaseConfig;
import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.yihui.common.po.Response;
import com.dafy.yihui.common.util.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liaoxudong
 * Date:2018/1/31
 */

public class UserServiceTest extends TestBaseConfig{

    @Autowired
    private IUserService userService;

//    @Before
    public void login(){
        Request request = new Request();
        request.put("username","admin");
        request.put("password", "123456");
        userService.login(request);
    }

    @Test
    public void testGetInfo(){
        Response info = userService.getInfo();

        System.out.println(JsonUtils.toJson(info));
    }


    @Test
    public void testAddUser(){
        /*user.setUserName(request.getString("username"));
        user.setPhoneno(request.getString("phoneNo"));
        user.setEmail(request.getString("email"));
        user.setNickName(request.getString("nickname"));
        user.setStatus(request.getBoolean("status")?1:0);
        // 默认密码123456
//        DigistUtils.getMD5String(user.getPhoneno()+"123456");
        user.setPwd(request.getString("password"));
        userMapper.insertSelective(user);
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(request.getLong("roleId"));*/
        Request request = new Request();
        request.put("username", "dasda");
        request.put("phoneNo", "dasda");
        request.put("email", "dasda");
        request.put("status", "dasda");
        request.put("nickname", "dasda");
        userService.add(request);
    }

    @Test
    public void testGetUserInfos(){
        Response userInfos = userService.findUserInfos(1, 50, 1);
        System.out.println(userInfos);
    }
}
