package com.dafy.lxp.ms.service.user;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.yihui.common.po.Response;

/**
 *
 * 用户操作日志
 * Created by liaoxudong
 * Date:2018/4/3
 */

public interface IUserOpLogService {

    Response list(Request request);
}
