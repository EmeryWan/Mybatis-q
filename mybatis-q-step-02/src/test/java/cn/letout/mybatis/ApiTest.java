package cn.letout.mybatis;

import cn.letout.mybatis.binding.MapperProxyFactory;
import cn.letout.mybatis.binding.MapperRegistry;
import cn.letout.mybatis.dao.IUserDao;
import cn.letout.mybatis.session.SqlSession;
import cn.letout.mybatis.session.SqlSessionFactory;
import cn.letout.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ApiTest {

    @Test
    public void testMapperProxyFactory() {
        // 1. 注册 Mapper
        // 通过注册机扫描包路径，注册映射器代理对象
        MapperRegistry registry = new MapperRegistry();
        registry.addMappers("cn.letout.mybatis.dao");

        // 2. 从 SqlSession 工厂获取 Session
        // 将注册机传递给 sqlSessionFactory 工厂，完成链接的过程
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 3. 获取映射器对象
        // 通过 Session 获取对应的 DAO 类型的实现类
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 4. 测试验证
        String res = userDao.queryUserName("10001");
        log.info("测试结果：{}", res);
    }

    @Test
    public void testProxyClass() {
        IUserDao userDao = (IUserDao) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{IUserDao.class},
                (((proxy, method, args) -> "你被代理了！"))
        );
        String result = userDao.queryUserName("1");
        log.info("测试结果：{}", JSON.toJSONString(result));
    }

}
