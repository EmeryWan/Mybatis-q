package cn.letout.mybatis;

import cn.letout.mybatis.dao.IUserDao;
import cn.letout.mybatis.io.Resources;
import cn.letout.mybatis.po.User;
import cn.letout.mybatis.session.SqlSession;
import cn.letout.mybatis.session.SqlSessionFactory;
import cn.letout.mybatis.session.SqlSessionFactoryBuilder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

@Slf4j
public class ApiTest {

    @Test
    public void testMapperProxyFactory() throws IOException {
        // 1. 从 SqlSessionFactory 中获取 SqlSession
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        //for (int i = 0; i < 50; i++) {
            User user = userDao.queryUserInfoById(1L);
            log.info("测试结果：{}", JSON.toJSONString(user));
        //}
    }

}
