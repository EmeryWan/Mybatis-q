package cn.letout.mybatis.session;

import java.sql.Connection;

/**
 * 事务隔离级别
 */
public enum TransactionIsolationLevel {

    // 0
    NONE(Connection.TRANSACTION_NONE),

    // 读未提交 1
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

    // 读已提交 2
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

    // 可重复读 4
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

    // 可串行化 8
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),

    ;

    private final int level;

    TransactionIsolationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}
