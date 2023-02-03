package cn.letout.mybatis.type;

import cn.letout.mybatis.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 模板方法模式，定义标准流程
 */
public abstract class BaseTypeHandler<T> implements TypeHandler<T> {

    protected Configuration configuration;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }


    /**
     * 定义抽象方法，由子类实现不同类型的属性参数（StringTypeHandler、LongTypeHandler）
     * 为了流程清晰，这里占时只有一个设置非空参数
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        setNonNullParameter(ps, i, parameter, jdbcType);
    }

    /**
     * 设置非空参数
     */
    protected abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

}
