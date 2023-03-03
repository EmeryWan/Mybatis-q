package cn.letout.mybatis.session;

import java.util.List;

/**
 * 用于：（1）定义执行 SQL 标准、（2）获取映射器对象、（3）后续管理事务 等操作
 */
public interface SqlSession {

    // 这里的 statement 其实是 SQL 对应的接口的全限定名 id
    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object parameter);

    <E> List<E> selectList(String statement, Object parameter);

    int insert(String statement, Object parameter);

    int update(String statement, Object parameter);

    Object delete(String statement, Object parameter);

    void commit();

    Configuration getConfiguration();

    <T> T getMapper(Class<T> type);

}
