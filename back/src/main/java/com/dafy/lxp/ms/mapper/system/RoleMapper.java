package com.dafy.lxp.ms.mapper.system;

import com.dafy.lxp.ms.dto.system.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

//    @Select("select r.* from T_ROLE r inner join T_USER_ROLE ur on r.id=ur.role_id where ur.user_id=#{userId}")
    Role getRoleByUserId(@Param("userId") Long userId);

    List<Role> getAllRoles();

    List<Role> findRoleInfos(Map<String, Object> map);
}