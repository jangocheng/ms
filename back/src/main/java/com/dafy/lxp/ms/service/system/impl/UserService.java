package com.dafy.lxp.ms.service.system.impl;

import com.dafy.lxp.ms.common.constant.Constants;
import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.common.domain.ResponseCode;
import com.dafy.lxp.ms.dto.system.Permission;
import com.dafy.lxp.ms.dto.system.Role;
import com.dafy.lxp.ms.dto.system.User;
import com.dafy.lxp.ms.dto.system.UserRole;
import com.dafy.lxp.ms.mapper.system.UserMapper;
import com.dafy.lxp.ms.mapper.system.UserRoleMapper;
import com.dafy.lxp.ms.service.system.IMenuService;
import com.dafy.lxp.ms.service.system.IPermissionService;
import com.dafy.lxp.ms.service.system.IRoleService;
import com.dafy.lxp.ms.service.system.IUserService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.yihui.common.cache.CacheFactory;
import com.dafy.yihui.common.db.po.Page;
import com.dafy.yihui.common.po.Response;
import com.dafy.yihui.common.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录操作
 * Created by liaoxudong
 * Date:2018/1/30
 */

@Service
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Response login(Request request) {
        String username = request.getString("username");
        String password = request.getString("password");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try{
            subject.login(token);
        } catch (AuthenticationException e) {
            logger.error("登录失败：",e);
            CommonUtils.throwException(ResponseCode.LOGIN_ERROR);
        }
        String loginToken = StringUtils.getUUID();
        // 模拟理享派用户登录
        CacheFactory.putHKey(com.dafy.yihui.common.constant.Constants.CACHE_TOKEN_PREFIX, loginToken,System.currentTimeMillis());
        CacheFactory.putString(Constants.LXP_LOGINED_TOKEN_KEY,loginToken);
        return CommonUtils.buildResp(ResponseCode.LOGIN_SUCCESS);
    }

    @Override
    public User getUser(String userName, String password) {
        return userMapper.findUserByName(userName,password);
    }

    @Override
    public Response getInfo() {
        //从session获取用户信息
        Session session = SecurityUtils.getSubject().getSession();
        User userInfo = (User) session.getAttribute(Constants.USER_INFO_SESSION_KEY);
        // FIXME Shiro登录session存的信息过期 先用固定用户的方式
        if (userInfo == null) {
            CommonUtils.throwException(ResponseCode.LOGIN_EXPIRE);
        }
//        userInfo = userMapper.selectByPrimaryKey(1L);
//        String username = userInfo.getUserName();
        Map<String, Object> resultMap = new HashMap<>();
        List<Permission> permissionList = permissionService.getUserPermissions(userInfo.getId());
        // 获取用户基本信息
        resultMap.put("userInfo", userInfo);
        // 获取用户权限列表
        resultMap.put("permissionList", permissionList);
        // 获取用户菜单列表
        resultMap.put("menuTrees", menuService.getUserMenus(userInfo.getId()));
        // 角色信息
        resultMap.put("roleInfo", roleService.getRoleByUserId(userInfo.getId()));
        // 将权限信息加入session
        session.setAttribute(Constants.USER_PERMISSION_SESSION_KEY, permissionList);
//        response.setData(userPermission);
        return CommonUtils.buildSuccessResp(resultMap);
    }

    @Override
    public Response logout() {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
        } catch (Exception e) {
        }
        return CommonUtils.buildResp(ResponseCode.SUCCESS);
    }

    @Override
    public Response listUser(int currentPage, int pageSize) {
        Page page = new Page<>(currentPage, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        userMapper.findUsers(map);
        return CommonUtils.buildSuccessResp(map.get("page"));
    }

    @Override
    public Response findUserInfos(int currentPage, int pageSize, long userId) {
        Page page = new Page<>(currentPage, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("userId", userId);
        userMapper.findUserInfos(map);
        return CommonUtils.buildSuccessResp(map.get("page"));
    }

    @Override
    public Response getAllRoles() {
        List<Role> allRoles = roleService.getAllRoles();
        return CommonUtils.buildSuccessResp(allRoles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response add(Request request) {
        User user = new User();
        user.setUserName(request.getString("username"));
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
        userRole.setRoleId(request.getLong("roleId"));
        userRoleMapper.insertSelective(userRole);
        return CommonUtils.buildSuccessResp();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response update(Request request) {
        long id = request.getLong("userId");
        String nickname = request.getString("nickname");
        long roleId = request.getLong("roleId");
        String password = request.getString("password");
        boolean status = request.getBoolean("status");
        User user = new User();
        user.setId(id);
        user.setPwd(StringUtils.isEmpty(password)?null:password);
        user.setNickName(nickname);
        user.setStatus(status?1:0);
        userMapper.updateByPrimaryKeySelective(user);
        UserRole userRole = new UserRole();
        userRole.setUserId(id);
        userRole.setRoleId(roleId);
        userRoleMapper.updateByUserId(userRole);
        return CommonUtils.buildSuccessResp();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response delete(User request) {
        userMapper.deleteByPrimaryKey(request.getId());
        userRoleMapper.deleteByUserId(request.getId());
        return CommonUtils.buildSuccessResp();
    }
}
