package com.dafy.lxp.ms.config.shiro;

import com.dafy.lxp.ms.common.constant.Constants;
import com.dafy.lxp.ms.dto.system.Permission;
import com.dafy.lxp.ms.dto.system.User;
import com.dafy.lxp.ms.service.system.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liaoxudong
 * Date:2018/1/30
 */

public class UserAuthRealm extends AuthorizingRealm{
    @Autowired
    private IUserService loginService;

    /**
     * 授权处理 @RequirePermission()的AOP调用
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Session session = SecurityUtils.getSubject().getSession();
        List<Permission> permissionList = (List)session.getAttribute(Constants.USER_PERMISSION_SESSION_KEY);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permissions = new HashSet<>();
        permissionList.forEach(p -> {
            permissions.add(p.getpCode());
        });
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 认证处理 subject.login()调用
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String loginName = (String) authenticationToken.getPrincipal();
        // 获取用户密码
        String password = new String((char[]) authenticationToken.getCredentials());
        User user = loginService.getUser(loginName, password);
        if (user == null) {// 账号或密码不正确
            throw new UnknownAccountException();
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现

        AuthenticationInfo authenticationInfo = new MutiAccountAuthenticationInfo(user,getName());
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        /*SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUserName(),
                user.getPwd(),
                //ByteSource.Util.bytes("salt"), salt=username+salt,采用明文访问时，不需要此句
                getName()
        );*/
        //session中不需要保存密码
        user.setPwd("");
        //将用户信息放入session中
        SecurityUtils.getSubject().getSession().setAttribute(Constants.USER_INFO_SESSION_KEY, user);
        return authenticationInfo;
    }
}
