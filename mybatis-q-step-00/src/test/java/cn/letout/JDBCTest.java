package cn.letout;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCTest {

    @Test
    public void testJDBC() throws Exception {
        // 1. 加载驱动
        Class.forName("com.mysql.jdbc.Driver");

        // 2. 连接信息
        String url = "jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true&characterEncoding=utf8&useSSL=true";
        String username = "root";
        String password = "123456";

        // 3. 获取连接
        Connection connection = DriverManager.getConnection(url, username, password);

        // 4. 执行 SQL 的对象获取
        Statement statement = connection.createStatement();

        // 5. 代执行的 SQL
        String sql = "SELECT id, user_id, user_name, user_head FROM user";

        // 6. 获取结果
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println(
                    "id=" + resultSet.getObject("id") + ", " +
                    "user_id=" + resultSet.getObject("user_id") + ", " +
                    "user_name=" + resultSet.getObject("user_name") + ", " +
                    "user_head=" + resultSet.getObject("user_head")
            );
        }

        // 7. 释放连接
        resultSet.close();
        statement.close();
        connection.close();
    }

}
