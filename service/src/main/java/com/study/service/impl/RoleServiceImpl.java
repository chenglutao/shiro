package com.study.service.impl;

import com.study.common.entity.Key;
import com.study.common.entity.PageEntity;
import com.study.common.entity.PageUtil;
import com.study.common.entity.RespEntity;
import com.study.repository.dao.generate.RoleMapper;
import com.study.repository.dao.generate.RoleMenuMapper;
import com.study.repository.dao.generate.UserRoleMapper;
import com.study.repository.entity.generate.*;
import com.study.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author chenglutao
 * @date 2019-12-20 12:51
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Role> getRoleByUserName(String userName) {
        return roleMapper.getRoleByUserName(userName);
    }

    @Override
    public List<Role> list() {
        RoleExample example = new RoleExample();
        example.setOrderByClause("id ASC");
        return roleMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void add(Role role, String menuIds) {
        roleMapper.insertSelective(role);
        addRoleMenu(role.getId(), menuIds);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void update(Role role, String menuIds) {
        roleMapper.updateByPrimaryKeySelective(role);
        RoleMenuExample example = new RoleMenuExample();
        RoleMenuExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(role.getId());
        roleMenuMapper.deleteByExample(example);
        addRoleMenu(role.getId(), menuIds);
    }

    @Override
    public RespEntity delete(String roleId) {
        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(Integer.valueOf(roleId));
        List<UserRole> userRoleList = userRoleMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            return RespEntity.error("角色删除失败,角色下存在用户数据!");
        }
        userRoleMapper.deleteByExample(example);
        roleMapper.deleteByPrimaryKey(Integer.valueOf(roleId));
        RoleMenuExample roleMenuExample = new RoleMenuExample();
        RoleMenuExample.Criteria cr = roleMenuExample.createCriteria();
        cr.andRoleIdEqualTo(Integer.valueOf(roleId));
        roleMenuMapper.deleteByExample(roleMenuExample);
        return RespEntity.ok();
    }

    @Override
    public Role detail(String roleId) {
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(Integer.valueOf(roleId));
        List<Role> list = roleMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public PageEntity<Role> page(String roleName, String pageSize, String pageNum) {
        PageEntity<Role> pageEntity = new PageEntity<Role>(null, 0);
        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("pageSize", pageSize);
        pageMap.put("curPage", pageNum);
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(roleName)){
            criteria.andNameEqualTo(roleName);
        }
        example.setOrderByClause("order_by asc");
        int total = roleMapper.countByExample(example);
        List<Role> roleList = roleMapper.selectByExampleWithRowbounds(example, PageUtil.getRowBounds(pageMap));
        if (CollectionUtils.isNotEmpty(roleList)) {
            pageEntity.setTotal(total);
            pageEntity.setKey(Key.OK);
            pageEntity.setMsg("查询成功");
            pageEntity.setData(roleList);
        }
        return pageEntity;
    }

    public void addRoleMenu(Integer roleId, String menuIds) {
        String[] menu = menuIds.split(",");
        Arrays.stream(menu).forEach(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(Integer.valueOf(menuId));
            roleMenuMapper.insert(roleMenu);
        });
    }
}
