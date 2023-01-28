# 细化 XML 语句构建器，完善静态 SQL 解析

## 细化 XML 语句构建器


## 完善静态 SQL 解析

```xml
<!-- 静态 SQL -->
<select id="queryUserInfoById" parameterType="java.lang.Long" resultType="cn.letout.mybatis.po.User">
    SELECT id, userId, userName, userHead
    FROM user
    where id = #{id}
</select>
```
