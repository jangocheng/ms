package com.dafy.lxp.ms.mapper.system;

import com.dafy.lxp.ms.dto.system.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    @Update("update T_USER_ROLE set role_id=#{roleId} where user_id=#{userId}")
    void updateByUserId(UserRole userRole);

    @Delete("delete from T_USER_ROLE where user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long id);

    @Delete("delete from T_USER_ROLE where role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long id);
}