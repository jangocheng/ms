package com.dafy.lxp.ms.service.pub;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.yihui.common.po.Response;

/**
 * 用于管理系统页面查询客户端日志
 * Created by liaoxudong
 * Date:2018/3/2
 */

public interface IClientLogService {

    /**
     * 获取日志列表
     * @param request 请求报文
     * @return
     */
    Response list(Request request);
}
