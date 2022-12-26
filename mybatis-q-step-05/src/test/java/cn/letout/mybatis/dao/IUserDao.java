package cn.letout.mybatis.dao;

import cn.letout.mybatis.po.User;

public interface IUserDao {

    User queryUserInfoById(Long uId);

}
