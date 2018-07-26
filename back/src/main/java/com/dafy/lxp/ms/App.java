package com.dafy.lxp.ms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by liaoxudong
 * Date:2018/1/26
 */

@SpringBootApplication(scanBasePackages = {"com.dafy.yihui.common", "com.dafy.lxp.ms"})
@ServletComponentScan
//启注解事务管理
@EnableTransactionManagement
@MapperScan("com.dafy.lxp.ms.mapper")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
//        new SpringApplication(App.class).run(args);
    }
}
