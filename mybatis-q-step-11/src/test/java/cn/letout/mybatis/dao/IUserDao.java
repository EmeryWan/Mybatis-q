package cn.letout.mybatis.dao;

import cn.letout.mybatis.po.User;

import java.util.List;

public interface IUserDao {

    User queryUserInfoById(Long uId);

    User queryUserInfo(User req);

    List<User> queryUserInfoList();

    int updateUserInfo(User req);

    int insertUserInfo(User req);

    int deleteUserInfoByUserId(String userId);

}
