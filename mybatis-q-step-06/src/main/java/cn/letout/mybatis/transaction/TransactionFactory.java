package cn.letout.mybatis.transaction;

import cn.letout.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 事务工厂（工厂方法模式：为每一个事务实现，都提供一个对应的工厂）
 */
public interface TransactionFactory {

    /**
     * 根据连接创建事务
     */
    Transaction newTransaction(Connection connection);

    /**
     * 根据 数据源 事务隔离级别 是否自动提交 创建事务
     */
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);

}
