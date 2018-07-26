package com.dafy.lxp.ms.api.loan;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.service.loan.ILoanSpvService;
import com.dafy.yihui.common.po.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/2/2
 */

@RestController
@RequestMapping("/loan/spv")
public class SpvApi {
    private final static Logger logger = LoggerFactory.getLogger(SpvApi.class);

    @Autowired
    private ILoanSpvService loanSpvService;

    @GetMapping("/list")
    @RequiresPermissions("spv:list")
    public Response list(@RequestHeader Map header){
        int currentPage = Integer.parseInt(header.get("pagenum").toString());
        int pageCount = Integer.parseInt(header.get("pagerow").toString());
        int status = Integer.parseInt(header.get("status").toString());
        String date = header.get("loandate") == null?"":header.get("loandate").toString();
        logger.debug("pageInfo,currentPage:{},pageCount:{}",currentPage,pageCount);
        return loanSpvService.list(currentPage,pageCount,status==0?null:status,date);

    }


    @PostMapping("/downSpv")
    @RequiresPermissions("spv:down")
    public Response downSpv(@RequestBody Request request){
        return loanSpvService.downSpv(request);
    }
}
