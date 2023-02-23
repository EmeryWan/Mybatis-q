package cn.letout.mybatis.mapping;

import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.type.JdbcType;

/**
 * 参数映射
 */
public class ParameterMapping {

    private Configuration configuration;

    private String property;  // 参数名称，eg: id

    private Class<?> javaType = Object.class;

    private JdbcType jdbcType;


    public static class Builder {

        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
        }

        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            parameterMapping.jdbcType = jdbcType;
            return this;
        }

    }

}
