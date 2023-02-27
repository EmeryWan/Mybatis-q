package cn.letout.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 策略模式
 * 策略之一
 */
public class StringTypeHandler extends BaseTypeHandler<String> {

    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        // i -> 第 i 个参数
        ps.setString(i, parameter);
    }

}
