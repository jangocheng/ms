package com.dafy.lxp.ms.service.user.impl;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.pub.ClientLog;
import com.dafy.lxp.ms.mapper.user.UserOpLogMapper;
import com.dafy.lxp.ms.service.user.IUserOpLogService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.yihui.common.db.po.Page;
import com.dafy.yihui.common.po.Response;
import com.dafy.yihui.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户操作日志
 * Created by liaoxudong
 * Date:2018/4/3
 */

@Service
public class UserOpLogService implements IUserOpLogService {

    @Autowired
    private UserOpLogMapper userOpLogMapper;

    @Override
    public Response list(Request request) {
        int pageNum = request.getInt("pagenum");
        int pageRow = request.getInt("pagerow");
//        request.getString("uuid");
        String phoneNo = request.getString("phoneNo");
//        request.getString("title");
//        request.getString("content");
        String date = request.getString("date");
        Page<ClientLog> page = new Page<>(pageNum, pageRow);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        if (!StringUtils.isEmpty(phoneNo)) {
            map.put("phoneNo", phoneNo);
        }
        if (!StringUtils.isEmpty(date)) {
            map.put("date", date);
        }
        userOpLogMapper.selectList(map);
        return CommonUtils.buildSuccessResp(page);
    }
}
