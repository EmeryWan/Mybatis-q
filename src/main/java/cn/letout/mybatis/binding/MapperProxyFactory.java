package cn.letout.mybatis.binding;

import cn.letout.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单工厂模式
 *
 * 映射器的代理工厂，是代理类 MapperProxy 包装，对外提供工厂实例化操作 MapperProxyFactory#newInstance
 * 给每一个操作数据库的接口映射器注册代理时，为每一个 IDAO 接口生成代理类
 */
public class MapperProxyFactory<T> {

    // 给哪个接口进行代理
    private final Class<T> mapperInterface;

    private Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Map<Method, MapperMethod> getMethodCache() {
        return methodCache;
    }

    /**
     * 工厂的目的就是把代理的创建给封装起来
     * 如果不进行这一层的封装，每次创建代理类，都需要使用 Proxy.newProxyInstance 进行处理，很麻烦
     */
    @SuppressWarnings("unchecked")
    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);

        // 代理操作
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

}
