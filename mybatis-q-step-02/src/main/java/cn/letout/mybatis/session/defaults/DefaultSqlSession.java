package cn.letout.mybatis.session.defaults;

import cn.letout.mybatis.binding.MapperRegistry;
import cn.letout.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    /**
     * 映射器注册机
     */
    private MapperRegistry mapperRegistry;

    public DefaultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！方法：" + statement);
    }

    /**
     * 暂时先简单返回
     * 目前没有和数据库进行关联，后续实现
     */
    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("你被代理了！" + "方法：" + statement + " 入参：" + parameter);
    }

    /**
     * 暂时通过 mapperRegistry 获取，后续替换
     */
    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperRegistry.getMapper(type, this);
    }

}
