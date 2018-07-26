package com.dafy.lxp.ms.common.datasource;

import com.dafy.yihui.common.db.dataSource.DynamicDataSourceGlobal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 用于多模块事务处理，只支持主服务的数据库事务
 * 即当前模块只支持User库的事务
 * Created by liaoxudong
 * Date:2017/11/8
 */

public class MultiModuleDSTransManager extends DataSourceTransactionManager {
    private static final Logger logger = LoggerFactory.getLogger(MultiModuleDSTransManager.class);
    public MultiModuleDSTransManager(MultiModuleDataSource multiModuleDataSource) {
        super(multiModuleDataSource);
    }

    /**
     * 只读事务到读库，读写事务到写库
     * @param transaction
     * @param definition
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {

        //设置数据源
        boolean readOnly = definition.isReadOnly();
        String dataSourceKey = MultiDataSource.DEFAULT.getCode()+"_";
        if(readOnly) {
            dataSourceKey += DynamicDataSourceGlobal.READ.name();
        } else {
            dataSourceKey += DynamicDataSourceGlobal.WRITE.name();
        }
        MultiModuleDSHolder.setDataSourceKey(dataSourceKey);
        logger.debug("开始动态数据源事务处理");
        super.doBegin(transaction, definition);
    }

    /**
     * 清理本地线程的数据源
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        logger.debug("完成动态数据源事务处理，清空当前绑定数据源");
        MultiModuleDSHolder.clearDataSource();
    }
}
