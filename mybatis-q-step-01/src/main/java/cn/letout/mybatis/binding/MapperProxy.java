package cn.letout.mybatis.binding;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 映射器代理类（最终的代理类）
 * 包装对数据库的操作
 * @param <T>
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -4127882999811942529L;

    // 传入的参数（后期完善）
    private Map<String, String> sqlSession;

    // 给哪个接口进行代理
    private final Class<T> mapperInterface;

    public MapperProxy(Map<String, String> sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    /**
     * 封装操作逻辑，对外接口提供数据库操作对象
     * 暂时提供一个简单的包装，模拟对数据库的调用（最终所有的实际调用都会用到这个方法包装的逻辑）
     *
     * Object proxy -> 代理的对象
     * Method method -> 正在调用的方法
     * Object[] args -> 正在调用的方法的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 排除 Object 中的通用方法，不进行代理
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        // cn.letout.mybatis.dao.IUserDao + . + queryUserName
        return "你被代理了！" + sqlSession.get(mapperInterface.getName() + "." + method.getName());
    }
}
