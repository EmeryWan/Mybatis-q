package cn.letout.mybatis;

import cn.letout.mybatis.binding.MapperProxyFactory;
import cn.letout.mybatis.dao.IUserDao;
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
        MapperProxyFactory<IUserDao> factory = new MapperProxyFactory<>(IUserDao.class);

        // 模拟 sqlSession
        // （在框架中，真正的为开启数据库连接，在事务下，对数据库进行操作（ORM框架的真正内容））
        Map<String, String> sqlSession = new HashMap<>();
        sqlSession.put("cn.letout.mybatis.dao.IUserDao.queryUserName", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户姓名");
        // sqlSession.put("cn.letout.mybatis.dao.IUserDao.queryUserAge", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户年龄");


        IUserDao userDao = factory.newInstance(sqlSession);

        userDao.toString();

        String name = userDao.queryUserName("10001");
        // Integer age = userDao.queryUserAge("100001");

        log.info("测试结果：{}", name);
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
