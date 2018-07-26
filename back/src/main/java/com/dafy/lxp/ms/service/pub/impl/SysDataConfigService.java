package com.dafy.lxp.ms.service.pub.impl;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.pub.SysDataConfigWithBLOBs;
import com.dafy.lxp.ms.mapper.pub.SysDataConfigMapper;
import com.dafy.lxp.ms.service.pub.ISysDataConfigService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.yihui.common.cache.CacheFactory;
import com.dafy.yihui.common.po.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/2/7
 */

@Service
public class SysDataConfigService implements ISysDataConfigService {

    @Autowired
    private SysDataConfigMapper sysDataConfigMapper;
    @Override
    public Response list(Map<String, Object> header) {
        List<SysDataConfigWithBLOBs> all = sysDataConfigMapper.findAll();
        return CommonUtils.buildSuccessResp(all);
    }

    @Override
    public Response update(Request request) {
        SysDataConfigWithBLOBs config = new SysDataConfigWithBLOBs();
        config.setId(request.getLong("id"));
        config.setData(request.getString("data"));
        config.setData1(request.getString("data1"));
        sysDataConfigMapper.updateByPrimaryKeySelective(config);
        // FIXME 删除原来的缓存
        CacheFactory.deleteString("mybatis:cache:com.dafy.yihui.pub.mapper.SysConfigMapper");
        return CommonUtils.buildSuccessResp();
    }
}
