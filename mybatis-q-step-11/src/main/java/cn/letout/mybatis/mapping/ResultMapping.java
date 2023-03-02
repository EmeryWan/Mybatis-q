package cn.letout.mybatis.mapping;

import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.type.JdbcType;
import cn.letout.mybatis.type.TypeHandler;

/**
 * 结果映射
 *
 * 于执行操作的 参数映射 ParameterMapping 类似
 */
public class ResultMapping {

    private Configuration configuration;

    private String property;

    private String column;

    private Class<?> javaType;

    private JdbcType jdbcType;

    private TypeHandler<?> typeHandler;


    private ResultMapping() {
    }


    public static class Builder {
        private ResultMapping resultMapping = new ResultMapping();

    }

}
