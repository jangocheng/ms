package com.dafy.lxp.ms.exception;

import com.dafy.lxp.ms.common.domain.ResponseCode;
import com.dafy.yihui.common.constant.Constants;
import com.dafy.yihui.common.exception.ServerException;
import com.dafy.yihui.common.po.Response;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User模块统一异常处理
 * 统一返回json数据
 * Created by liaoxudong
 * Date:2017/10/27
 */

@ControllerAdvice
public class ExceptionHandlerCenter {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerCenter.class);
    /**
     * 自定义异常处理
     *
     * @param serverException
     * @return
     */
    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response processException(ServerException serverException) {
        logger.warn("自定义异常处理:{}", serverException.getMessage());
        Response resp = new Response(4);
        resp.setCode(serverException.getCode());
        resp.setMsg(serverException.getDesc());
        return resp;
    }

    /**
     * jdbcException处理
     * @param jdbcException
     * @return
     *//*
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response processException(SQLException jdbcException) {
        logger.info("JDBC exception!", jdbcException);
        Response resp = new Response(4);
        resp.setCode(Constants.ERROR_CODE);
        if (jdbcException instanceof MySQLIntegrityConstraintViolationException) {
            resp.setMsg("在唯一索引列上添加重复数据！");
        }else{
            resp.setMsg("数据库操作异常");
        }
        return resp;
    }*/


    /**
     * RuntimeException处理
     *
     * @param runtimeException
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(RuntimeException runtimeException) {
        Response resp = new Response(4);
        resp.setCode(Constants.ERROR_CODE);
        logger.error("运行时异常处理："+ runtimeException.getMessage(),runtimeException);
        if (runtimeException instanceof DuplicateKeyException) {
            resp.setMsg("在唯一索引列上添加重复数据！");
        } else if(runtimeException instanceof HttpMessageNotReadableException) {
            resp.setMsg("参数错误，请检查");
        } else if(runtimeException instanceof UnauthenticatedException){
            resp.setCode(ResponseCode.LOGIN_EXPIRE.getCode());
            resp.setMsg(ResponseCode.LOGIN_EXPIRE.getDesc());
        }else{
            resp.setMsg(runtimeException.getMessage());
        }
        return resp;
    }

    /**
     * 其它异常处理
     *
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(Exception exception) {
        logger.error("服务器异常："+exception.getMessage(), exception);
        Response resp = new Response(4);
        resp.setCode(Constants.ERROR_CODE_50X);
        if (exception instanceof HttpMediaTypeNotSupportedException) {
            resp.setMsg("不支持的媒体格式，请尝试[Content-Type:application/json]");
        }else{
            resp.setMsg("服务器异常："+exception.getMessage());
        }
        // FIXME 短信发送暂时不用了，信息太少也看不出什么东西
        /*Sms sms = buildAlarmSms(exception, "Pub", "13148899469");
        smsService.sendMsg(sms,Constants.ALARM_SMS_IP);*/
        return resp;
    }

}
