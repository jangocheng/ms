package com.dafy.lxp.ms.mapper.system;

import com.dafy.lxp.ms.dto.system.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    List<Menu> getAllMenus();

    List<Menu> getAllMenus(Map<String, Object> map);
}