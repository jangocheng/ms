package com.dafy.lxp.ms.service.system.impl;

import com.dafy.lxp.ms.common.domain.Request;
import com.dafy.lxp.ms.dto.system.Menu;
import com.dafy.lxp.ms.mapper.system.MenuMapper;
import com.dafy.lxp.ms.service.system.IMenuService;
import com.dafy.lxp.ms.utils.CommonUtils;
import com.dafy.lxp.ms.utils.TreeUtils;
import com.dafy.yihui.common.db.po.Page;
import com.dafy.yihui.common.po.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liaoxudong
 * Date:2018/1/31
 */

@Service
public class MenuService implements IMenuService{
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<Menu> getUserMenus(Long userId) {
        List<Menu> menus = menuMapper.getAllMenus();
        //List<MenuTreeDto> menuTreeDtos = MenuTreeDto.convert(menus);
//        MenuTreeUtils menuTreeUtils = new MenuTreeUtils();
        List<Menu> menuTrees = TreeUtils.buildTree(menus);
//        List<Menu> menuTrees = menuTreeUtils.buildTree(menus);
        return menuTrees;
    }

    @Override
    public Response list(Map header) {
        int pageNum = Integer.valueOf(header.get("pagenum").toString());
        int pageRow = Integer.valueOf(header.get("pagerow").toString());
        Page<Menu> page = new Page<>(pageNum, pageRow);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        menuMapper.getAllMenus(map);
        /**
         *
         */
        List<Menu> menuList = page.getData();
        page.setData(TreeUtils.buildTree(menuList));
        return CommonUtils.buildSuccessResp(page);
    }

    @Override
    public Response add(Request request) {
        Menu menu = new Menu();
        menu.setmCode(request.getString("mCode"));
        menu.setmName(request.getString("mName"));
        menu.setmIcon(request.getString("mIcon"));
        menu.setmOrder(request.getInt("mOrder"));
        menu.setParentId(request.getLong("parentId"));
        menu.setmUrl(request.getString("mUrl"));
        menu.setDesc(request.getString("desc"));
        menuMapper.insertSelective(menu);
        return CommonUtils.buildSuccessResp();
    }

    @Override
    public Response delete(Long id) {
        return null;
    }

    @Override
    public Response update(Request request) {
        return null;
    }
}
