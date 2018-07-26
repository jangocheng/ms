package com.dafy.lxp.ms.mapper.system;

import com.dafy.lxp.ms.dto.system.RoleMenu;

public interface RoleMenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);

    RoleMenu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleMenu record);

    int updateByPrimaryKey(RoleMenu record);
}