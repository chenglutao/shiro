package com.study.service;

import com.study.common.entity.PageEntity;
import com.study.common.entity.RespEntity;
import com.study.repository.entity.generate.Menu;

import java.util.List;

/**
 * @author chenglutao
 */
public interface MenuService {

    /**
     * 查找用户权限集
     * @param userName 用户名
     * @return 用户权限集合
     */
    List<Menu> getUserPermissions(String userName);

    /**
     * 查找用户菜单集合
     * @param userName 用户名
     * @return 用户菜单集合
     */
    List<Menu> getUserMenus(String userName);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     * @return List<Menu>
     */
    List<Menu> list();

    Integer add(Menu menu);

    Integer update(Menu menu);

    RespEntity delete(String menuId);

    PageEntity<Menu> page(String menuName, String pageSize, String pageNum);

    Menu detail(String menuId);
}
