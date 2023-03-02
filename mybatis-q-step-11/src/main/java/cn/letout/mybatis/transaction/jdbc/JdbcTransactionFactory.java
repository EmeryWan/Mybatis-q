package cn.letout.mybatis.transaction.jdbc;

import cn.letout.mybatis.session.TransactionIsolationLevel;
import cn.letout.mybatis.transaction.Transaction;
import cn.letout.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * JdbcTransaction 工厂
 */
public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection connection) {
        return new JdbcTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }

}
