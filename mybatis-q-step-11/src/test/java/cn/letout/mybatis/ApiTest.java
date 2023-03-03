package cn.letout.mybatis;

import cn.letout.mybatis.dao.IUserDao;
import cn.letout.mybatis.io.Resources;
import cn.letout.mybatis.po.User;
import cn.letout.mybatis.session.SqlSession;
import cn.letout.mybatis.session.SqlSessionFactory;
import cn.letout.mybatis.session.SqlSessionFactoryBuilder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

@Slf4j
public class ApiTest {

    private SqlSession sqlSession;

    @Before
    public void init() throws IOException {
        // 1. 从 SqlSessionFactory 中获取 SqlSession
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void testInsert() {
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        User user = new User();
        user.setUserId("10002");
        user.setUserName("添加");
        user.setUserHead("2333");
        int u = userDao.insertUserInfo(user);
        log.info("insert: {}", u);

        sqlSession.commit();
    }

    @Test
    public void testDelete() {
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        int u = userDao.deleteUserInfoByUserId("10002");
        log.info("delete: {}", u);

        sqlSession.commit();
    }

    @Test
    public void testUpdate() {
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        User user = new User();
        user.setId(1L);
        user.setUserId("10001");
        user.setUserName("update new name");
        int u = userDao.updateUserInfo(user);
        log.info("update: {}", u);

        sqlSession.commit();
    }

    @Test
    public void testQuery() throws IOException {
        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // User user = userDao.queryUserInfoById(1L);
        User user = userDao.queryUserInfo(new User(1L, "10001"));
        log.info("测试结果：{}", JSON.toJSONString(user));
    }

}
