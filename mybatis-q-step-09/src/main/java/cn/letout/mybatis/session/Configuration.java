package cn.letout.mybatis.session;

import cn.letout.mybatis.binding.MapperRegistry;
import cn.letout.mybatis.datasource.druid.DruidDataSourceFactory;
import cn.letout.mybatis.datasource.pooled.PooledDataSourceFactory;
import cn.letout.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import cn.letout.mybatis.executor.Executor;
import cn.letout.mybatis.executor.SimpleExecutor;
import cn.letout.mybatis.executor.parameter.ParameterHandler;
import cn.letout.mybatis.executor.resultset.DefaultResultSetHandler;
import cn.letout.mybatis.executor.resultset.ResultSetHandler;
import cn.letout.mybatis.executor.statement.PreparedStatementHandler;
import cn.letout.mybatis.executor.statement.StatementHandler;
import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.Environment;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.reflection.MetaObject;
import cn.letout.mybatis.reflection.factory.DefaultObjectFactory;
import cn.letout.mybatis.reflection.factory.ObjectFactory;
import cn.letout.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import cn.letout.mybatis.reflection.wrapper.ObjectWrapperFactory;
import cn.letout.mybatis.scripting.LanguageDriver;
import cn.letout.mybatis.scripting.LanguageDriverRegistry;
import cn.letout.mybatis.scripting.xmltags.XMLLanguageDriver;
import cn.letout.mybatis.transaction.Transaction;
import cn.letout.mybatis.transaction.jdbc.JdbcTransactionFactory;
import cn.letout.mybatis.type.TypeAliasRegistry;
import cn.letout.mybatis.type.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 配置项
 * 包装 注册机和 SQL 语句
 * **所有内容都会在 Configuration 中串联流程
 */
public class Configuration {

    /**
     * 环境
     */
    protected Environment environment;

    /**
     * 映射注册机（用于注册 Mapper 映射器的操作类）
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 存放映射的语句
     * MappedStatement 用于记录 SQL 信息：SQL 类型、SQL 语句、入参类型、出参类型
     * key:id->cn.letout.mybatis.dao.IUserDao.queryUserInfoById
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * 类型别注册机
     * 用于 XML 中的配置 -> 解析成对应的 java class 类型
     */
    protected TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    protected LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();

    /**
     * 类型处理器注册机
     */
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    /**
     * 对象工厂 和 对象包装工厂
     */
    protected ObjectFactory objectFactory = new DefaultObjectFactory();

    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();

    /**
     * 存放已经加载过的资源
     * eg: mapper/UserMapper.xml
     */
    protected final Set<String> loadedResources = new HashSet<>();


    protected String databaseId;


    public Configuration() {
        // 添加 Jdbc、Druid 注册操作
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

        languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
    }

    //

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    //

    public void addMappedStatement(MappedStatement statement) {
        mappedStatements.put(statement.getId(), statement);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    //

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    //

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    //

    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 创建执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter, resultHandler, boundSql);
    }

    /**
     * 创建元对象
     */
    public MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory);
    }

    /**
     * 类型处理器注册机
     */
    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public LanguageDriverRegistry getLanguageRegistry() {
        return languageRegistry;
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        // 创建参数处理器
        ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
        // 插件的一些参数，也是在这里处理，暂时不添加这部分内容 interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }

    public LanguageDriver getDefaultScriptingLanguageInstance() {
        return languageRegistry.getDefaultDriver();
    }

}
