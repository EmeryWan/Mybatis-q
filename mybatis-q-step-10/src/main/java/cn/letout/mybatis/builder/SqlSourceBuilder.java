package cn.letout.mybatis.builder;

import cn.letout.mybatis.mapping.ParameterMapping;
import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.parsing.GenericTokenParser;
import cn.letout.mybatis.parsing.TokenHandler;
import cn.letout.mybatis.reflection.MetaClass;
import cn.letout.mybatis.reflection.MetaObject;
import cn.letout.mybatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SQL 源码构建器
 *
 * 用来具体处理 SQL 中的参数
 */
public class SqlSourceBuilder extends BaseBuilder {

    private static Logger log = LoggerFactory.getLogger(SqlSourceBuilder.class);

    private static final String parameterProperties = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
        // 获取 #{} 中的参数，将获取的参数封装为 ParameterMapping，并放入 BoundSql
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);

        // 解析 #{ }
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        // 返回静态 SQL
        return new StaticSqlSource(configuration, sql, handler.getParameterMappings());
    }


    //

    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        private List<ParameterMapping> parameterMappings = new ArrayList<>();

        private Class<?> parameterType;

        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType, Map<String, Object> additionalParameters) {
            super(configuration);
            this.parameterType = parameterType;
            this.metaParameters = configuration.newMetaObject(additionalParameters);
        }

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        @Override
        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        private ParameterMapping buildParameterMapping(String content) {
            // 解析映射参数 -> 转化成一个 HashMap
            // #{favouriteSection,jdbcType=VARCHAR}
            Map<String, String> propertiesMap = new ParameterExpression(content);
            String property = propertiesMap.get("property");

            Class<?> propertyType = parameterType;
            if (typeHandlerRegistry.hasTypeHandler(parameterType)) {  // 有对应的参数处理器 -> 基本类型
                propertyType = parameterType;
            } else if (property != null) {
                MetaClass metaClass = MetaClass.forClass(parameterType);
                if (metaClass.hasGetter(property)) {
                    propertyType = metaClass.getGetterType(property);
                } else {
                    propertyType = Object.class;
                }
            } else {  // 无参数
                propertyType = Object.class;
            }

            log.info("够建参数映射 property: {} propertyType: {}", property, propertyType);
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
            return builder.build();
        }
    }

}
