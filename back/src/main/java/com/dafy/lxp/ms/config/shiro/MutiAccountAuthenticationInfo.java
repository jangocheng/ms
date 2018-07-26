package com.dafy.lxp.ms.config.shiro;

import com.dafy.lxp.ms.dto.system.User;
import com.dafy.yihui.common.util.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.ArrayList;
import java.util.List;

/**
 * 多用户登录认证信息
 * Created by liaoxudong
 * Date:2018/1/30
 */

public class MutiAccountAuthenticationInfo implements AuthenticationInfo {
    private SimpleAuthenticationInfo simpleAuthenticationInfo;
    private List<String> usernames = new ArrayList<>();

    public MutiAccountAuthenticationInfo(User user, String realName) {
        buildMultiUserNames(user);
        simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                usernames,
                user.getPwd(),
                ByteSource.Util.bytes(user.getPhoneno()), //加盐
                realName
        );
    }

    // 支持多用户登录
    private void buildMultiUserNames(User user) {
        if (!StringUtils.isEmpty(user.getUserName())) {
            usernames.add(user.getUserName());
        }
        usernames.add(user.getPhoneno());
        if (!StringUtils.isEmpty(user.getEmail())) {
            usernames.add(user.getEmail());
        }
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return simpleAuthenticationInfo.getPrincipals();
    }

    @Override
    public Object getCredentials() {
        return simpleAuthenticationInfo.getCredentials();
    }
}
