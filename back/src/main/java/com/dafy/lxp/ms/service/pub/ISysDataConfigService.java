package com.dafy.lxp.ms.service.pub;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.yihui.common.po.Response;

import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/2/7
 */

public interface ISysDataConfigService {

    Response list(Map<String,Object> header);

    Response update(Request request);
}
