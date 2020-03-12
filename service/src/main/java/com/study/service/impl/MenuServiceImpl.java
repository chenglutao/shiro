package com.study.service.impl;

import com.study.common.entity.Key;
import com.study.common.entity.PageEntity;
import com.study.common.entity.PageUtil;
import com.study.common.entity.RespEntity;
import com.study.repository.dao.generate.MenuMapper;
import com.study.repository.dao.generate.RoleMenuMapper;
import com.study.repository.entity.generate.*;
import com.study.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Integer add(Menu menu) {
        return menuMapper.insertSelective(menu);
    }

    @Override
    public Integer update(Menu menu) {
        menu.setModifyTime(new Date());
        return menuMapper.updateByPrimaryKeySelective(menu);
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

    @Override
    public PageEntity<Menu> page(String menuName, String pageSize, String pageNum) {
        PageEntity<Menu> pageEntity = new PageEntity<Menu>(null, 0);
        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("pageSize", pageSize);
        pageMap.put("curPage", pageNum);
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(menuName)){
            criteria.andMenuNameEqualTo(menuName);
        }
        example.setOrderByClause("order_by asc");
        int total = menuMapper.countByExample(example);
        List<Menu> menusList = menuMapper.selectByExampleWithRowbounds(example, PageUtil.getRowBounds(pageMap));
        if (CollectionUtils.isNotEmpty(menusList)) {
            pageEntity.setTotal(total);
            pageEntity.setKey(Key.OK);
            pageEntity.setMsg("查询成功");
            pageEntity.setData(menusList);
        }
        return pageEntity;
    }

    @Override
    public Menu detail(String menuId) {
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(Integer.valueOf(menuId));
        List<Menu> list = menuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            return  null;
        }
        return list.get(0);
    }
}
