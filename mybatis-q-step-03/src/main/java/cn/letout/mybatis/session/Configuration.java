package cn.letout.mybatis.session;

import cn.letout.mybatis.binding.MapperRegistry;
import cn.letout.mybatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置项
 * 包装注册机和 SQL 语句
 */
public class Configuration {

    // 映射注册机（用于注册 Mapper 映射器的操作类）
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    // 存放映射的语句
    // MappedStatement 用于记录 SQL 信息：SQL 类型、SQL 语句、入参类型、出参类型
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

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

}
