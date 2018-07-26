package com.dafy.lxp.ms.service.loan;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.yihui.common.po.Response;

/**
 * Created by liaoxudong
 * Date:2018/2/2
 */

public interface ILoanSpvService {

    /**
     * 列出spv放款列表
     * @param currentPage 当前页
     * @param pageCount 每页显示条目
     * @param status 放款状态
     * @param date
     * @return
     */
    Response list(int currentPage, int pageCount, Integer status, String date);

    Response downSpv(Request request);


}
