package cn.letout.mybatis.session;

import cn.letout.mybatis.binding.MapperRegistry;
import cn.letout.mybatis.datasource.druid.DruidDataSourceFactory;
import cn.letout.mybatis.datasource.pooled.PooledDataSourceFactory;
import cn.letout.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import cn.letout.mybatis.mapping.Environment;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.transaction.jdbc.JdbcTransactionFactory;
import cn.letout.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

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

    // 存放映射的语句
    // MappedStatement 用于记录 SQL 信息：SQL 类型、SQL 语句、入参类型、出参类型
    // key:id->cn.letout.mybatis.dao.IUserDao.queryUserInfoById
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * 类型别注册机
     */
    protected TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        // 添加 Jdbc、Druid 注册操作
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
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

}
