package com.dafy.lxp.ms.common.datasource;

import java.lang.annotation.*;

/**
 * Created by liaoxudong
 * Date:2017/11/7
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TargetDataSource {

    MultiDataSource value() default MultiDataSource.DEFAULT;
}
