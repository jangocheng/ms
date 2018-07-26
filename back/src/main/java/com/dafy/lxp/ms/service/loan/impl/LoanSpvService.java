package com.dafy.lxp.ms.service.loan.impl;

import com.dafy.lxp.ms.common.constant.Constants;
import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.service.loan.ILoanSpvService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.yihui.common.cache.CacheFactory;
import com.dafy.yihui.common.po.Response;
import com.dafy.yihui.common.util.HttpUtils;
import com.dafy.yihui.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/2/2
 */

@Service
public class LoanSpvService implements ILoanSpvService{
    @Value("${modules.loan.spvListUrl}")
    private String spvListUrl;

    @Value("${modules.loan.spvDownUrl}")
    private String spvDownUrl;
    @Override
    public Response list(int currentPage, int pageCount, Integer status, String date) {
        StringBuilder stringBuilder = new StringBuilder(spvListUrl);
        if (status != null) {
            stringBuilder.append("&status=" + status);
        }
        if (!StringUtils.isEmpty(date)) {
            stringBuilder.append("&date=" + date);
        }
        String url = String.format(stringBuilder.toString(), currentPage, pageCount);
        Map<String,Object>  headers = new HashMap<>();
        headers.put("Token", CacheFactory.getString(Constants.LXP_LOGINED_TOKEN_KEY));
        Response response = HttpUtils.doGetWithHead(url,headers, Response.class);
        return response;
    }

    @Override
    public Response downSpv(Request request) {
        List<Integer> ids = request.getList("ids");
        String userCode = request.getString("userCode");
        String userName = request.getString("userName");
        ids.forEach(id -> {
            request.clear();
            request.put("id", id);
            request.put("operatorCode", userCode);
            request.put("operatorName", userName);
            request.put("moneyDownStatus", 31);
            Map<String,Object>  headers = new HashMap<>();
            headers.put("Token", CacheFactory.getString(Constants.LXP_LOGINED_TOKEN_KEY));
            Response response = HttpUtils.doPostWithHead(spvDownUrl, request,headers,Response.class);
            if (!"00".equals(response.getCode())) {
                CommonUtils.throwException(response.getCode().toString(),response.getMsg().toString());
            }
        });
        return CommonUtils.buildSuccessResp();
    }
}
