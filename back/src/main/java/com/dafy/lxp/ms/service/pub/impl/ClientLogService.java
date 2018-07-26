package com.dafy.lxp.ms.service.pub.impl;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.pub.ClientLog;
import com.dafy.lxp.ms.mapper.pub.ClientLogMapper;
import com.dafy.lxp.ms.service.pub.IClientLogService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.yihui.common.db.po.Page;
import com.dafy.yihui.common.po.Response;
import com.dafy.yihui.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by liaoxudong
 * Date:2018/3/2
 */

@Service
public class ClientLogService implements IClientLogService{

    @Autowired
    private ClientLogMapper clientLogMapper;

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
        clientLogMapper.selectList(map);
        return CommonUtils.buildSuccessResp(page);
    }
}
