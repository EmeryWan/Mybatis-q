package cn.letout.mybatis.mapping;

import cn.letout.mybatis.reflection.MetaObject;
import cn.letout.mybatis.session.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 绑定的 SQL
 * 从 SqlSource 而来，将动态内容都处理完后，得到 SQL 语句字符串（包括 ？、绑定的参数）
 */
public class BoundSql {

    private String sql;  // 格式化好的 SQL，参数被替换成了 ?

    private List<ParameterMapping> parameterMappings;  // 该 SQL 的所有参数信息：参数名、参数 java 类别、参数 jdbc 类别

    private Object parameterObject;  // 参数对象的值 eg: where id = #{1} 这里就是 1

    private Map<String, String> additionalParameters;

    private MetaObject metaParameters;


    public BoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings, Object parameterObject) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<>();
        this.metaParameters = configuration.newMetaObject(additionalParameters);
    }

    public String getSql() {
        return sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public Map<String, String> getAdditionalParameters() {
        return additionalParameters;
    }

    public MetaObject getMetaParameters() {
        return metaParameters;
    }
}
