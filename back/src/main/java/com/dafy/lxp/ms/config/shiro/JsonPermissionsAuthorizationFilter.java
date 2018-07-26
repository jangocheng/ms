package com.dafy.lxp.ms.config.shiro;

import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.lxp.ms.common.domain.ResponseCode;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 对没有登录的请求进行拦截, 全部返回json信息. 覆盖掉shiro原本的跳转login.jsp的拦截方式
 * Created by liaoxudong
 * Date:2018/1/30
 */

public class JsonPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter{

    /**
     * 登录过期
     * 返回json
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        PrintWriter out = null;
        HttpServletResponse res = (HttpServletResponse) response;
        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            out = response.getWriter();
            out.println(CommonUtils.buildSuccessResp(ResponseCode.LOGIN_EXPIRE));
        } catch (Exception e) {
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return false;
    }

    @Bean
    public FilterRegistrationBean registration(JsonPermissionsAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
}
