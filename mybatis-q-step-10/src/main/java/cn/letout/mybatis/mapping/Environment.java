package cn.letout.mybatis.mapping;

import cn.letout.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

public class Environment {

    // 环境 id
    private final String id;  // development

    // 事务工厂
    private final TransactionFactory transactionFactory;

    // 数据源  javax.sql.DataSource
    private final DataSource dataSource;  // 实际的 DataSource 实现类。在这里的实现有：cn.letout.mybatis.datasource -> (PooledDataSource UnpooledDataSource DruidDataSource)

    public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
        this.id = id;
        this.transactionFactory = transactionFactory;
        this.dataSource = dataSource;
    }

    public static class Builder {
        private String id;
        private TransactionFactory transactionFactory;
        private DataSource dataSource;

        public Builder(String id) {
            this.id = id;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory) {
            this.transactionFactory = transactionFactory;
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public String id() {
            return this.id;
        }

        public Environment build() {
            return new Environment(this.id, this.transactionFactory, this.dataSource);
        }
    }


    public String getId() {
        return id;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
