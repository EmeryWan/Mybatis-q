package cn.letout.mybatis.mapping;

import cn.letout.mybatis.session.Configuration;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 映射语句类
 * 用于记录 SQL 信息：SQL 类型、SQL 语句、入参类型、出参类型
 */
@Getter
@Setter
public class MappedStatement {

    private Configuration configuration;

    private String id;

    private SqlCommandType sqlCommandType;

    // private String parameterType;
    // private String resultType;
    // private String sql;
    // private Map<Integer, String> parameter;

    private BoundSql boundSql;

    // 禁用构造
    MappedStatement() {
    }

    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, BoundSql boundSql) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = boundSql;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }

}
