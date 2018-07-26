package com.dafy.lxp.ms.common.datasource;

import com.dafy.yihui.common.db.dataSource.DynamicDataSourceGlobal;
import com.dafy.yihui.common.exception.ExceptionCode;
import com.dafy.yihui.common.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 重写动态数据源，支持前期多个模块耦合在一个模块中，但是数据库分离的策略
 * Created by liaoxudong
 * Date:2017/11/8
 */

public class MultiModuleDataSource extends AbstractRoutingDataSource{
    private static final Logger logger = LoggerFactory.getLogger(MultiModuleDataSource.class);
    // 默认数据源，使用当前模块配置的主库，找不到时使用
//    private DataSource defaultDataSource;
    // 写数据源集合
    private Map<String,DataSource> writeDataSources;
    // 读数据源集合
    private Map<String,List<DataSource>> readDataSources;

    @Override
    public void afterPropertiesSet() {
        if (writeDataSources == null || writeDataSources.isEmpty() || readDataSources == null || readDataSources.isEmpty()) {
            throw new ServerException(ExceptionCode.DATASOURCE_CONFIG_ERROR);
        }
        // 各模块主库直接放入可用数据源列表
        Map<Object,Object> dataSources = new HashMap<>();
        dataSources.putAll(writeDataSources);
        Set<String> keySet = readDataSources.keySet();
        for (String key : keySet) {
            List<DataSource> dl = readDataSources.get(key);
            for(int i=0;i<dl.size();i++) {
                // 读库按 moduleName_READ_0/1/2加入可用数据源列表
                dataSources.put(key+"_" + i, dl.get(i));
            }
        }
        super.setTargetDataSources(dataSources);
        logger.debug("Available DataSources：{}",dataSources.keySet());
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceKey = MultiModuleDSHolder.getDataSourceKey();
        if (dataSourceKey == null) {
            throw new ServerException(ExceptionCode.DATASOURCE_ERROR);
        }
        // 写库直接读取
        /*if(dataSourceKey.contains(DynamicDataSourceGlobal.WRITE.name())){
            return dataSourceKey;
        }*/
        // 读库使用自定义策略，随机获取
        if (dataSourceKey.contains(DynamicDataSourceGlobal.READ.name())) {
            List<DataSource> dataSources = readDataSources.get(dataSourceKey);
            dataSourceKey = new MultiModuleStrategy(dataSourceKey).select(dataSources, null);
        }
        logger.debug("Current DataSource：{}",dataSourceKey);
        return dataSourceKey;
    }

    public void setWriteDataSources(Map<String, DataSource> writeDataSources) {
        this.writeDataSources = writeDataSources;
    }

    public void putWriteDataSource(String key, DataSource writeDataSource) {
        synchronized (this){
            if (this.writeDataSources == null) {
                writeDataSources = new ConcurrentHashMap<>();
            }
        }
        writeDataSources.put(key, writeDataSource);
    }

    public void setReadDataSources(Map<String, List<DataSource>> readDataSources) {
        this.readDataSources = readDataSources;
    }

    public void putReadDataSources(String key, DataSource readDataSource) {
        synchronized (this) {
            if (readDataSources == null) {
                readDataSources = new ConcurrentHashMap<>();
            }
        }
        if (readDataSources.get(key) != null) {
            readDataSources.get(key).add(readDataSource);
        }else{
            List<DataSource> rds = new ArrayList<>();
            rds.add(readDataSource);
            readDataSources.put(key, rds);
        }
    }

}
