package com.dafy.lxp.ms.config.sys;

import com.dafy.lxp.ms.common.datasource.MultiDataSource;
import com.dafy.lxp.ms.common.datasource.MultiModuleDSInterceptor;
import com.dafy.lxp.ms.common.datasource.MultiModuleDSTransManager;
import com.dafy.lxp.ms.common.datasource.MultiModuleDataSource;
import com.dafy.yihui.common.db.dataSource.DynamicDataSourceGlobal;
import com.dafy.yihui.common.db.mybatis.plugin.PageInterceptor;
import com.dafy.yihui.common.db.mybatis.plugin.SqlCostInterceptor;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by liaoxudong
 * Date:2017/11/8
 */

@Configuration
@EnableTransactionManagement
public class MultiDataSourceConfig extends MybatisAutoConfiguration{
    private static final Logger logger = LoggerFactory.getLogger(MultiDataSourceConfig.class);

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;


    public MultiDataSourceConfig(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        super(properties, interceptorsProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);
    }


    // 默认数据源，就是后台管理所用的数据库
    @Bean("defaultDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.ms.write")
    public DataSource buildDefaultDataSource(){
        DataSource defaultDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        return defaultDataSource;
    }

    // 默认读库  就是后台管理所用的数据库
    @Bean("defaultReadDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ms.read")
    public DataSource buildDefaultReadDataSource(){
        DataSource userReadDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        return userReadDataSource;
    }

    // 公共模块写库
    @Bean("publicWriteDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.public.write")
    public DataSource buildPublicWriteDataSource(){
        DataSource publicWriteDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        return publicWriteDataSource;
    }

    // 公共模块读库
    @Bean("publicReadDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.public.read")
    public DataSource buildPublicReadDataSource(){
        DataSource publicReadDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        return publicReadDataSource;
    }

    // 用户模块写库
    @Bean("userWriteDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user.write")
    public DataSource buildUserWriteDataSource(){
        DataSource userWriteDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        return userWriteDataSource;
    }

    // 用户模块读库
    @Bean("userReadDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user.read")
    public DataSource buildUserReadDataSource(){
        DataSource userReadDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        return userReadDataSource;
    }

    /**
     * 合同模块写库
     * @return
     */
    @Bean("loanWriteDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.loan.write")
    public DataSource buildLoanWriteDataSource(){
        DataSource loanWriteDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        return loanWriteDataSource;
    }

    /**
     * 合同模块读库
     * @return
     */
    @Bean("loanReadDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.loan.read")
    public DataSource buildLoanReadDataSource(){
        DataSource loanReadDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        return loanReadDataSource;
    }

    @Bean
    public MultiModuleDataSource buildMultiModuleDataSource(){
        MultiModuleDataSource multiModuleDataSource = new MultiModuleDataSource();
        // 默认数据源设置
        multiModuleDataSource.setDefaultTargetDataSource(buildDefaultDataSource());
        multiModuleDataSource.putWriteDataSource(MultiDataSource.DEFAULT.getCode()+"_"+ DynamicDataSourceGlobal.WRITE, buildDefaultDataSource());
        multiModuleDataSource.putReadDataSources(MultiDataSource.DEFAULT.getCode() + "_" + DynamicDataSourceGlobal.READ, buildDefaultReadDataSource());
        // 添加一众写库
        multiModuleDataSource.putWriteDataSource(MultiDataSource.USER.getCode()+"_"+ DynamicDataSourceGlobal.WRITE, buildUserWriteDataSource());
        multiModuleDataSource.putWriteDataSource(MultiDataSource.PUBLIC.getCode()+"_"+ DynamicDataSourceGlobal.WRITE, buildPublicWriteDataSource());
        multiModuleDataSource.putWriteDataSource(MultiDataSource.LOAN.getCode()+"_"+ DynamicDataSourceGlobal.WRITE, buildLoanWriteDataSource());

        multiModuleDataSource.putReadDataSources(MultiDataSource.USER.getCode() + "_" + DynamicDataSourceGlobal.READ, buildUserReadDataSource());
        multiModuleDataSource.putReadDataSources(MultiDataSource.PUBLIC.getCode() + "_" + DynamicDataSourceGlobal.READ, buildPublicReadDataSource());
        multiModuleDataSource.putReadDataSources(MultiDataSource.LOAN.getCode() + "_" + DynamicDataSourceGlobal.READ, buildLoanReadDataSource());
        return multiModuleDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        logger.info("初始化sqlSessionFactory");
        // 替换掉mybatis-config.xml的配置加载插件，动态数据源插件/分页插件(默认mysql分页)/sql执行时间统计插件
        Interceptor[] interceptors = {new MultiModuleDSInterceptor(),new PageInterceptor(),new SqlCostInterceptor()};
        logger.debug("已加载Mybatis拦截器数量：{}--[{}]",interceptors.length,interceptors);
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(buildMultiModuleDataSource());
        factory.setVfs(SpringBootVFS.class);
        factory.setPlugins(interceptors);
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return factory.getObject();
    }


//    默认事务
//    @Bean
//    public PlatformTransactionManager txManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }


    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() throws Exception{
        logger.info("初始化事务管理器");
        return new MultiModuleDSTransManager(buildMultiModuleDataSource());
    }


}
