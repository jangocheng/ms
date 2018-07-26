package com.dafy.lxp.ms.mapper.system;

import com.dafy.lxp.ms.dto.system.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    List<Permission> getUserPermissions(Long userId);

    List<Permission> findAllPage(Map<String, Object> map);

    List<Permission> findAll();
}