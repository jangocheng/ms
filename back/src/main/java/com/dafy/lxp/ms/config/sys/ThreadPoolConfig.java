package com.dafy.lxp.ms.config.sys;

import com.dafy.yihui.common.thread.pool.CusThreadPool;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 线程池配置
 * Created by liaoxudong
 * Date:2017/11/17
 */

@Configuration
@ConfigurationProperties(prefix = "config.threadPool")
public class ThreadPoolConfig {
    private int coreThreads;
    private int maxThreads;
    private int queueSize;

    @Bean("cusThreadPool")
    public CusThreadPool newCusThreadPool(){
        // 固定大小线程池
        return new CusThreadPool(coreThreads,maxThreads,queueSize);
    }


    public void setCoreThreads(int coreThreads) {
        this.coreThreads = coreThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
