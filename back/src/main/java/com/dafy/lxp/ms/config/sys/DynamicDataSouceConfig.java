package com.dafy.lxp.ms.config.sys;

import java.io.IOException;

/**
 * 动态数据源配置
 * Created by liaoxudong
 * Date:2017/11/13
 */

//@Configuration
//@EnableTransactionManagement
public class DynamicDataSouceConfig/* extends MybatisAutoConfiguration*/{
    /*private static final Logger logger = LoggerFactory.getLogger(DynamicDataSouceConfig.class);

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    public DynamicDataSouceConfig(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        super(properties, interceptorsProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);
    }


    *//**
     *
     * @return 写库数据源
     *//*
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.write")
    public DataSource writeDataSource(){
        DataSource master = DataSourceBuilder.create().type(dataSourceType).build();
        logger.debug("Created master Datasource");
        return master;
    }

    *//**
     *
     * @return 读库数据源
     *//*
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.read")
    public DataSource readDataSource(){
        DataSource slave = DataSourceBuilder.create().type(dataSourceType).build();
        logger.debug("Created slave Datasource");
        return slave;
    }

    *//**
     * @return 动态数据源
     *//*
    @Bean
    public DynamicDataSource dynamicDataSource(){
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setMasterDataSource(writeDataSource());
        dataSource.setReadDataSource(readDataSource());
        logger.debug("动态数据源可用数量：[{}]",dataSource.availableDataSources().size());
        return dataSource;
    }

    *//**
     * @return sqlSessionFactory
     * @throws Exception
     *//*
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        logger.debug("Created sqlSessionFactory");
        // 替换掉mybatis-config.xml的配置加载插件，动态数据源插件/分页插件(默认mysql分页)/sql执行时间统计插件
        Interceptor[] interceptors = {new DynamicDataSourceInterceptor(),new PageInterceptor(),new SqlCostInterceptor()};
        logger.debug("已加载Mybatis拦截器数量：{}--[{}]",interceptors.length,interceptors);
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dynamicDataSource());
        factory.setVfs(SpringBootVFS.class);
        factory.setPlugins(interceptors);
        factory.setMapperLocations(resolveMapperLocations());
        return factory.getObject();
    }

    *//**
     * 加载mapper配置
     * 参见：MybatisProperties下的同名方法
     * @return
     * @throws IOException
     *//*
    private Resource[] resolveMapperLocations() throws IOException {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        return resourceResolver.getResources(mapperLocations);
        *//*List<Resource> resources = new ArrayList();
        if (mapperLocations != null) {
            String[] var3 = this.mapperLocations;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String mapperLocation = var3[var5];

                try {
                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                } catch (IOException var8) {
                    ;
                }
            }
        }

        return (Resource[])resources.toArray(new Resource[resources.size()]);*//*
    }

    *//**
     *
     * @return 事务管理器 注解启用事务
     *//*
    @Bean
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager(){
        logger.debug("Created transactionManager");
        return new DynamicDataSourceTransactionManager(dynamicDataSource());
    }*/


}
