package cn.letout.mybatis.session;

/**
 * 用于：
 * （1）定义执行 SQL 标准
 * （2）获取映射器 Mapper 对象
 * （3）后续管理事务
 *  等 操作
 */
public interface SqlSession {

    /**
     * 根据指定的 sqlId 获取一条记录的封装对象
     *
     * @param statement sqlId
     * @return 封装后的对象 Mapped Object
     * @param <T> 封装后的对象类型
     */
    <T> T selectOne(String statement);

    /**
     * 根据指定的 sqlId 获得一条记录的封装对象，可以给 sql 传递一些参数
     * 在实际使用中，一般传递的参数是：pojo / Map / ImmutableMap
     */
    <T> T selectOne(String statement, Object parameter);


    //


    Configuration getConfiguration();

    /**
     * 得到映射器（这里巧妙地使用了泛型，使得类型安全）
     */
    <T> T getMapper(Class<T> type);

}
