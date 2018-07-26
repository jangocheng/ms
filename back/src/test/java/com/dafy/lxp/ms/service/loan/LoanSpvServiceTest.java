package com.dafy.lxp.ms.service.loan;

import com.dafy.lxp.ms.TestBaseConfig;
import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.yihui.common.po.Response;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaoxudong
 * Date:2018/2/2
 */

public class LoanSpvServiceTest extends TestBaseConfig{

    @Autowired
    private ILoanSpvService loanSpvService;
    @Test
    public void spvList(){
        Response list = loanSpvService.list(1, 50, null, "");
        System.out.println(list);
    }

    @Test
    public void downSpv(){
        Request request = new Request();
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        request.put("ids", ids);
        request.put("userCode", "xxx");
        request.put("userName", "yyyy");
        loanSpvService.downSpv(request);

    }
}
