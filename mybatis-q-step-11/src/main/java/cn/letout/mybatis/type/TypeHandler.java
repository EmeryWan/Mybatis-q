package cn.letout.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器
 * 定义标准接口
 */
public interface TypeHandler<T> {

    /**
     * 设置参数
     * i -> 第几个参数
     * parameter -> 参数值
     */
    void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

    /**
     * 获取结果
     */
    T getResult(ResultSet rs, String columnName) throws SQLException;

}
