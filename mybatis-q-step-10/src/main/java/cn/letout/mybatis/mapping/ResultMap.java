package cn.letout.mybatis.mapping;

import cn.letout.mybatis.session.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 结果信息映射
 */
public class ResultMap {

    private String id;  // 标识结果类型的唯一 id

    private Class<?> type;

    private List<ResultMapping> resultMappings;  // 结果映射集

    private Set<String> mappedColumns;


    private ResultMap() {
    }


    public static class Builder {
        private ResultMap resultMap = new ResultMap();

        public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings) {
            resultMap.id = id;
            resultMap.type = type;
            resultMap.resultMappings = resultMappings;
        }

        public ResultMap build() {
            resultMap.mappedColumns = new HashSet<>();
            return resultMap;
        }
    }


    public String getId() {
        return id;
    }

    public Class<?> getType() {
        return type;
    }

    public List<ResultMapping> getResultMappings() {
        return resultMappings;
    }

    public Set<String> getMappedColumns() {
        return mappedColumns;
    }

}
