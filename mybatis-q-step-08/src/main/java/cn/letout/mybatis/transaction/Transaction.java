package cn.letout.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 定义事务接口
 * 具体由不同的事务方式进行实现，包括：（1）JDBC (2)托管事务（如交给 Spring 这样的容器管理）
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

}
