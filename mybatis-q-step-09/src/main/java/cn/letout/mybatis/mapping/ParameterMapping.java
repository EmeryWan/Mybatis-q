package cn.letout.mybatis.mapping;

import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.type.JdbcType;
import cn.letout.mybatis.type.TypeHandler;
import cn.letout.mybatis.type.TypeHandlerRegistry;

/**
 * 参数映射
 *
 * javaType <---> jdbcType
 */
public class ParameterMapping {

    private Configuration configuration;

    private String property;  // 参数名称，eg: id

    private Class<?> javaType = Object.class;  // 参数的 java 类型，eg: java.lang.Long

    private JdbcType jdbcType;

    private TypeHandler<?> typeHandler;  // 类型处理器，用于SQL参数设置，不同的参数类型有不同的类型处理器，注册在 TypeHandlerRegistry，根据类型（策略）获取不同的处理器

    private ParameterMapping() {
    }


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
            if (parameterMapping.typeHandler == null && parameterMapping.javaType != null) {
                Configuration configuration = parameterMapping.configuration;
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                // 策略模式，根据不同的类型（策略），获得不同的 typeHandler（LongTypeHandler / StringTypeHandler）
                parameterMapping.typeHandler = typeHandlerRegistry.getTypeHandler(parameterMapping.javaType, parameterMapping.jdbcType);
            }

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

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

}
