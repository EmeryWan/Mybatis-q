package cn.letout.mybatis.session;

/**
 * 用于：（1）定义执行 SQL 标准、（2）获取映射器对象、（3）后续管理事务 等操作
 */
public interface SqlSession {

    /**
     * 根据指定的 SqlID 获取一条记录的封装对象
     *
     * @param statement sqlID
     * @return Mapped object 封装之后的对象
     * @param <T> 封装之后的对象类型
     */
    <T> T selectOne(String statement);

    /**
     * 根据指定的 SqlID 获益一条记录的封装对象，可以给 sql 传递一些参数
     * 一般在实际使用中，这个参数传递的是 pojo / Map / ImmutableMap
     *
     * @param statement
     * @param parameter
     * @return
     * @param <T>
     */
    <T> T selectOne(String statement, Object parameter);

    /**
     * 得到映射器（这里巧妙地使用了泛型，使得类型安全）
     * @param type
     * @return
     * @param <T>
     */
    <T> T getMapper(Class<T> type);

}
