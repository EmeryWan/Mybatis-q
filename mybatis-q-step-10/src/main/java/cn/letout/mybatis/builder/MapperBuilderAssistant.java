package cn.letout.mybatis.builder;

import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.mapping.ResultMap;
import cn.letout.mybatis.mapping.SqlCommandType;
import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.scripting.LanguageDriver;
import cn.letout.mybatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 映射器构建助手
 * 专门为创建 MappedStatement 映射语句类服务
 */
public class MapperBuilderAssistant extends BaseBuilder {

    private String currentNamespace;

    private String resource;  // "mapper/UserMapper.xml"

    public MapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        }
        if (isReference) {
            if (base.contains(".")) {
                return base;
            }
        }

        return currentNamespace + "." + base;
    }


    public MappedStatement addMappedStatement(
            String id,
            SqlSource sqlSource,
            SqlCommandType sqlCommandType,
            Class<?> parameterType,
            String resultMap,
            Class<?> resultType,
            LanguageDriver lang
    ) {
        // 给 id 加上 namespace 前缀 -> cn.letout.mybatis.dao.IUserDao.queryUserInfoById
        id = applyCurrentNamespace(id, false);
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlCommandType, sqlSource, resultType);

        // 结果映射
        setStatementResultMap(resultMap, resultType, statementBuilder);

        // 构造语句信息
        MappedStatement statement = statementBuilder.build();

        configuration.addMappedStatement(statement);

        return statement;
    }


    private void setStatementResultMap(
            String resultMap,
            Class<?> resultType,
            MappedStatement.Builder statementBuilder
    ) {
        // 占时没有在 Mapper XML 中配置 Map 返回结果，这里暂时返回 null
        resultMap = applyCurrentNamespace(resultMap, true);

        List<ResultMap> resultMaps = new ArrayList<>();

        // <select id="queryUserInfoById" resultType="cn.bugstack.mybatis.test.po.User">
        // 使用 resultType 的情况下，MyBatis 会自动创建一个 ResultMap，基于属性名称映射到 JavaBean 的属性上
        //
        if (resultMap != null) {
            // TODO：暂无Map结果映射配置，后续实现

        } else if (resultType != null) {
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
                    configuration,
                    statementBuilder.id() + "-Inline",
                    resultType,
                    new ArrayList<>()
            );
            resultMaps.add(inlineResultMapBuilder.build());
        }
        statementBuilder.resultMaps(resultMaps);
    }

}
