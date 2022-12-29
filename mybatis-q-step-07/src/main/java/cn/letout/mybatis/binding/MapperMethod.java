package cn.letout.mybatis.binding;

import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.mapping.SqlCommandType;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.session.SqlSession;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * 映射方法器
 */
public class MapperMethod {

    private final SqlCommand command;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (command.getType()) {
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case SELECT:
                result = sqlSession.selectOne(command.getName(), args);
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command.getName());
        }
        return result;
    }

    @Getter
    public static class SqlCommand {
        private final String name;  // id->cn.letout.mybatis.dao.IUserDao.queryUserInfoById
        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName = mapperInterface.getName() + "." + method.getName();
            MappedStatement ms = configuration.getMappedStatement(statementName);
            this.name = ms.getId();
            this.type = ms.getSqlCommandType();
        }
    }
}
