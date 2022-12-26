package cn.letout.mybatis.session.defaults;

import cn.letout.mybatis.binding.MapperRegistry;
import cn.letout.mybatis.executor.Executor;
import cn.letout.mybatis.mapping.Environment;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.session.SqlSession;
import cn.letout.mybatis.session.SqlSessionFactory;
import cn.letout.mybatis.session.TransactionIsolationLevel;
import cn.letout.mybatis.transaction.Transaction;
import cn.letout.mybatis.transaction.TransactionFactory;

import java.sql.SQLException;

/**
 * 默认的 SqlSessionFactory
 *
 * 传递 mapperRegistry 创建 DefaultSession，就可以在使用 SqlSession 时获取每个代理类的映射器对象
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
            // 创建执行器
            final Executor executor = configuration.newExecutor(tx);
            // 创建 DefaultSqlSession
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session. Cause: " + e);
        }
    }

}
