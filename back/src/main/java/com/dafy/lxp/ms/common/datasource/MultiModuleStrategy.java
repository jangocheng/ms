package com.dafy.lxp.ms.common.datasource;

import com.dafy.yihui.common.db.strategy.Strategy;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

/**
 * Created by liaoxudong
 * Date:2017/11/8
 */

public class MultiModuleStrategy implements Strategy {
    private Object lock = new Object();
    private String dataSourceKey;

    public MultiModuleStrategy(String dataSourceKey) {
        this.dataSourceKey = dataSourceKey;
    }

    public MultiModuleStrategy() {
    }

    public void setDataSourceKey(String dataSourceKey) {
        this.dataSourceKey = dataSourceKey;
    }

    @Override
    public String select(List<DataSource> slaves, DataSource master) {
        int index = 0;
        synchronized (lock) {
            Random random = new Random();
            index = random.nextInt(slaves.size());
        }

        return dataSourceKey + "_"+ index;
    }
}
