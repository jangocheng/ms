package com.dafy.lxp.ms.mapper.system;

import com.dafy.lxp.ms.dto.system.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
//@CacheNamespace(implementation = RedisMybatisCache.class)
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    long insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findUserByName(@Param("userName") String userName, @Param("password") String password);

    List<User> findUsers(Map<String, Object> map);

    List<User> findUserInfos(Map<String, Object> map);
}