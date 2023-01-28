package cn.letout.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import cn.letout.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 映射器注册机：
 * 提供包路径的扫描和映射器代理类注册机服务
 * 完成接口对象的代理类注册处理
 */
public class MapperRegistry {

    // 存放已添加的映射器代理
    // key: cn.letout.mybatis.dao.IUserDao
    // value: dao 接口对应的 MapperProxyFactory，用来生成对应接口的代理对象
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    /**
     * 获取代理对象，包装手动操作实例化
     * 更加方便地在 DefaultSqlSession 中获取 Mapper 时进行使用
     */
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not know to the MapperRegistry.");
        }

        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    /**
     * 添加一个 dao 接口对应的 代理对象生成工厂
     */
    public <T> void addMapper(Class<T> type) {
        // Mapper 是接口才会注册
        if (type.isInterface()) {
            if (hasMapper(type)) {
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            // 注册映射器代理工厂
            knownMappers.put(type, new MapperProxyFactory<>(type));
        }
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    /**
     * 包路径的扫描，给类接口创建 MapperProxyFactory 映射器代理类，加入到 knowMappers 的 HashMap 缓存中
     */
    public <T> void addMappers(String packageName) {
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }

}
