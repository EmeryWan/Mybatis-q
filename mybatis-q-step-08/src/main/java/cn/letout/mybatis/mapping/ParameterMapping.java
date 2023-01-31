package cn.letout.mybatis.mapping;

import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.type.JdbcType;

/**
 * 参数映射
 *
 * javaType <---> jdbcType
 */
public class ParameterMapping {

    private Configuration configuration;

    private String property;  // 参数名称，eg: id

    private Class<?> javaType = Object.class;  // 参数的 java 类型，eg: java.lang.Long

    private JdbcType jdbcType;  //


    public static class Builder {

        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property, Class<?> javaType) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
            parameterMapping.javaType = javaType;
        }

        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            parameterMapping.jdbcType = jdbcType;
            return this;
        }

        public ParameterMapping build() {
            return parameterMapping;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }
}
