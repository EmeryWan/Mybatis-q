package cn.letout.mybatis.scripting.defaults;

import cn.letout.mybatis.executor.parameter.ParameterHandler;
import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.mapping.ParameterMapping;
import cn.letout.mybatis.reflection.MetaObject;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.type.JdbcType;
import cn.letout.mybatis.type.TypeHandler;
import cn.letout.mybatis.type.TypeHandlerRegistry;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 默认的参数处理器
 */
public class DefaultParameterHandler implements ParameterHandler {

    private Logger log = LoggerFactory.getLogger(DefaultParameterHandler.class);


    private final TypeHandlerRegistry typeHandlerRegistry;

    private final MappedStatement mappedStatement;

    private final Object parameterObject;

    private BoundSql boundSql;

    private Configuration configuration;

    public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
    }


    @Override
    public Object getParameterObject() {
        return parameterObject;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        // SQL 有参数
        // 遍历 ParameterMapping 集合，设置参数值
        if (null != parameterMappings) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String propertyName = parameterMapping.getProperty();

                Object value;
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {  // 有对应的参数处理器 -> 基本类型
                    value = parameterObject;
                } else {  // 对象类型
                    // 通过 MetaObject.getValue 反射取得值设置
                    MetaObject metaObject = configuration.newMetaObject(parameterObject);
                    value = metaObject.getValue(propertyName);
                }
                JdbcType jdbcType = parameterMapping.getJdbcType();

                // 设置参数
                log.info("根据每个ParameterMapping中的TypeHandler设置对应的参数信息 value：{}", JSON.toJSONString(value));
                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                typeHandler.setParameter(ps, i + 1, value, jdbcType);
            }
        }
    }

}
