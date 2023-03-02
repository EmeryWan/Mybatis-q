package cn.letout.mybatis.executor;

import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.session.ResultHandler;
import cn.letout.mybatis.session.RowBounds;
import cn.letout.mybatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * 执行器抽象基类
 * 模板模式
 *
 * 封装了执行器的全部接口，具体的子类继承抽象类后，就不用再处理这些共性的方法
 *
 * Mybatis 还有一些缓存的操作也实现在该类中，占时忽略
 */
public abstract class BaseExecutor implements Executor {

    private Logger log = LoggerFactory.getLogger(BaseExecutor.class);

    protected Configuration configuration;

    protected Transaction transaction;

    protected Executor wrapper;

    protected boolean closed;

    protected BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = this;
    }


    //


    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
    }

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql);

    @Override
    public Transaction getTransaction() {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return transaction;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new RuntimeException("Cannot commit, transaction is already closed.");
        }
        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed) {
            if (required) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                transaction.close();
            }
        } catch (SQLException e) {
            log.warn("Unexpected exception on closing transaction. Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }
}
