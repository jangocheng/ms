package com.dafy.lxp.ms.common.datasource;

import com.dafy.yihui.common.db.dataSource.DynamicDataSourceGlobal;
import com.dafy.yihui.common.exception.ExceptionCode;
import com.dafy.yihui.common.exception.ServerException;
import com.dafy.yihui.common.util.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扩展动态数据源插件，支持多模块耦合
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class }) })
public class MultiModuleDSInterceptor implements Interceptor {

    protected static final Logger logger = LoggerFactory.getLogger(MultiModuleDSInterceptor.class);

    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    private static final Map<String, String> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if(!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement ms = (MappedStatement) objects[0];
            String msId = ms.getId();

//            MultiModuleDSHolder lookUpKeyHolder = null;
            String dataSouceKey = null;

//            DynamicDataSourceGlobal dynamicDataSourceGlobal = null;
            String mapperClassname = msId.substring(0, msId.lastIndexOf("."));
            Class<?> mapperClass = Class.forName(mapperClassname);
            String dataSourceKeyPrefix = null;
            if (mapperClass.isAnnotationPresent(TargetDataSource.class)) {
                dataSourceKeyPrefix = mapperClass.getAnnotation(TargetDataSource.class).value().getCode();
                logger.warn("指定了特殊数据源：{}",dataSourceKeyPrefix);

            }else{// 默认数据源
                dataSourceKeyPrefix = MultiDataSource.DEFAULT.getCode();
            }
            if (StringUtils.isEmpty(dataSourceKeyPrefix)) {
                throw new ServerException(ExceptionCode.DATASOURCE_CONFIG_ERROR);
            }
            dataSourceKeyPrefix = dataSourceKeyPrefix + "_";

            if((dataSouceKey = cacheMap.get(msId)) == null) {
                //读方法
                if(ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                    //!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
                    if(ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                        dataSouceKey = DynamicDataSourceGlobal.WRITE.name();
                    } else {
                        BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                        String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                        if(sql.matches(REGEX)) {
                            dataSouceKey = DynamicDataSourceGlobal.WRITE.name();
                        } else {
                            dataSouceKey = DynamicDataSourceGlobal.READ.name();
                        }
                    }
                }else{
                    dataSouceKey = DynamicDataSourceGlobal.WRITE.name();
                }
                dataSouceKey = dataSourceKeyPrefix+dataSouceKey;
                cacheMap.put(msId, dataSouceKey);
            }
            logger.info("设置方法[{}] use [{}] Strategy, SqlCommandType [{}]..", msId, dataSouceKey, ms.getSqlCommandType().name());
            MultiModuleDSHolder.setDataSourceKey(dataSouceKey);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        //
    }
}
