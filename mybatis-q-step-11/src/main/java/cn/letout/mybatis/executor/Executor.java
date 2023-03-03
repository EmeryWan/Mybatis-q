package cn.letout.mybatis.executor;

import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.session.ResultHandler;
import cn.letout.mybatis.session.RowBounds;
import cn.letout.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * 执行器接口
 * 定义标准的执行过程：执行方法，事务获取，提交、回滚、关闭的定义
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    // insert delete update 都使用 update 方法
    int update(MappedStatement ms, Object parameter) throws SQLException;

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}
