package com.study.service.impl;

import com.study.common.entity.RespEntity;
import com.study.repository.dao.generate.MenuMapper;
import com.study.repository.dao.generate.RoleMenuMapper;
import com.study.repository.entity.generate.Menu;
import com.study.repository.entity.generate.MenuExample;
import com.study.repository.entity.generate.RoleMenuExample;
import com.study.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-27 16:50
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> getUserPermissions(String userName) {
        return menuMapper.getUserPermissions(userName);
    }

    @Override
    public List<Menu> getUserMenus(String userName) {
        return menuMapper.getUserMenus(userName);
    }

    @Override
    public List<Menu> list() {
        MenuExample example = new MenuExample();
        example.setOrderByClause("id ASC");
        List<Menu> menuList = menuMapper.selectByExample(example);
        return menuList;
    }

    @Override
    public void add(Menu menu) {
        menuMapper.insertSelective(menu);
    }

    @Override
    public void update(Menu menu) {
        menu.setModifyTime(new Date());
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
    public RespEntity delete(String menuId) {
        Menu menu = menuMapper.selectByPrimaryKey(Integer.valueOf(menuId));
        if (menu != null) {
            return RespEntity.error("菜单存在子菜单，不允许删除!");
        }
        menuMapper.deleteByPrimaryKey(Integer.valueOf(menuId));
        RoleMenuExample example = new RoleMenuExample();
        RoleMenuExample.Criteria criteria = example.createCriteria();
        criteria.andMenuIdEqualTo(Integer.valueOf(menuId));
        roleMenuMapper.deleteByExample(example);
        return RespEntity.ok();
    }
}
