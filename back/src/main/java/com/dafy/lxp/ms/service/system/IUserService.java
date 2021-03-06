package com.dafy.lxp.ms.service.system;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.system.User;
import com.dafy.yihui.common.po.Response;

/**
 * 登录相关
 * Created by liaoxudong
 * Date:2018/1/30
 */

public interface IUserService {


    /**
     * 登录
     * @param request username,password
     * @return
     */
    Response login(Request request);


    /**
     * 获取用户信息
     * @param userName
     * @param password
     * @return
     */
    User getUser(String userName, String password);

    /**
     * 获取已登录用户的全部授权基础信息
     * @return
     */
    Response getInfo();

    /**
     * 登出
     * @return
     */
    Response logout();

    /**
     * 查询所有用户
     * @param currentPage
     * @param pageCount
     * @return
     */
    Response listUser(int currentPage, int pageCount);

    Response findUserInfos(int currentPage, int pageSize, long userId);

    /**
     * 获取所有角色信息
     * @return
     */
    Response getAllRoles();

    /**
     * 新增用户
     * @param request
     * @return
     */
    Response add(Request request);

    /**
     * 更新用户信息
     * @param request
     * @return
     */
    Response update(Request request);

    /**
     * 删除用户
     * @param request
     * @return
     */
    Response delete(User request);
}
