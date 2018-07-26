package com.dafy.lxp.ms.mapper.system;

import com.dafy.lxp.ms.dto.system.RolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RolePermission record);

    int inserts(@Param("records") List<RolePermission> records);

    int insertSelective(RolePermission record);

    RolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RolePermission record);

    int updateByPrimaryKey(RolePermission record);

    @Delete("delete from T_ROLE_PERMISSION where role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long id);
}