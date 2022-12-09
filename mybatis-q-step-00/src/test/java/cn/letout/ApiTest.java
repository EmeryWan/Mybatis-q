package cn.letout;

import cn.letout.dao.IActivityDao;
import cn.letout.po.Activity;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

@Slf4j
public class ApiTest {

    @Test
    public void testSqlSessionFactory() throws IOException {

        // 1. 构建 SqlSessionFactory，以中获取 SqlSession

        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);


        // 2. 请求对象

        Activity req = new Activity();
        req.setActivityId(1000001L);


        // 3. SqlSession 获取代理对象，进行查询

        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        IActivityDao activityDao1 = sqlSession1.getMapper(IActivityDao.class);
        Activity activityRes1 = activityDao1.queryActivityById(req);
        log.info("测试结果1：{}", JSON.toJSONString(activityRes1));
        sqlSession1.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        IActivityDao activityDao2 = sqlSession2.getMapper(IActivityDao.class);
        Activity activityRes2 = activityDao2.queryActivityById(req);
        log.info("测试结果2：{}", JSON.toJSONString(activityRes2));
        sqlSession2.close();
    }

}
