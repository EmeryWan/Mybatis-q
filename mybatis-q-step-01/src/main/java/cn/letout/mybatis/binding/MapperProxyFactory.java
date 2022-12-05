package cn.letout.mybatis.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 映射器的代理工厂(简单工厂模式)
 * 为代理类 MapperProxy 的包装，对外提供工厂实例化操作 MapperProxyFactory#newInstance
 * 给每一个操作数据库的接口映射器注册代理的时候，为每一个 IDAO 接口生成代理类
 */
public class MapperProxyFactory<T> {

    /**
     * 给哪个接口进行代理（通过构造方法传入）
     */
    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * 工厂操作相当于把代理的创建给封装起来
     * 如果不进行这层封装，那么每一个创建代理类的操作，都需要使用 Proxy.newProxyInstance 进行处理，比较麻烦
     */
    public T newInstance(Map<String, String> sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface);

        // 代理操作
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
