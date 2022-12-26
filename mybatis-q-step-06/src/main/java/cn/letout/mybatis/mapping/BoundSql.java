package cn.letout.mybatis.mapping;

import lombok.Getter;

import java.util.Map;

/**
 * 绑定的 SQL
 * 从 SqlSource 而来，将动态内容都处理完后，得到 SQL 语句字符串（包括 ？、绑定的参数）
 */
@Getter
public class BoundSql {

    private String sql;  // 格式化好的 SQL，参数被替换成了 ?

    private Map<Integer, String> parameterMappings;

    private String parameterType;

    private String resultType;

    public BoundSql(String sql, Map<Integer, String> parameterMappings, String parameterType, String resultType) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterType = parameterType;
        this.resultType = resultType;
    }

}
