package cn.letout.mybatis.binding;

import cn.letout.mybatis.session.SqlSession;
import cn.letout.mybatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 映射器代理类（最终的代理类）
 * 包装对数据库的操作
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -4127882999811942529L;

    private SqlSession sqlSession;

    // 给哪个接口进行代理
    private final Class<T> mapperInterface;

    private final Map<Method, MapperMethod> methodCache;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
    }

    /**
     * 封装操作逻辑，对外接口提供数据库操作对象
     * 暂时提供一个简单的包装，模拟对数据库的调用（最终所有的实际调用都会用到这个方法包装的逻辑）
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 排除 Object 中的通用方法，不进行代理
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        final MapperMethod mapperMethod = cachedMapperMethod(method);
        return mapperMethod.execute(sqlSession, args);
    }

    // 去缓存中查找 MapperMethod
    private MapperMethod cachedMapperMethod(Method method) {
        MapperMethod mapperMethod = methodCache.get(method);
        // 找不到 new 一个
        if (mapperMethod == null) {
            mapperMethod = new MapperMethod(mapperInterface, method, sqlSession.getConfiguration());
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }
}
