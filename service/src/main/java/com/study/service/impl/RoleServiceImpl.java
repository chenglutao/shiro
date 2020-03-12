package com.study.service.impl;

import com.study.common.entity.RespEntity;
import com.study.repository.dao.generate.RoleMapper;
import com.study.repository.dao.generate.RoleMenuMapper;
import com.study.repository.dao.generate.UserRoleMapper;
import com.study.repository.entity.generate.*;
import com.study.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    public void add(Role role, List<Integer> menuIds) {
        roleMapper.insertSelective(role);
        for (Integer menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void update(Role role, List<Integer> menuIds) {
        roleMapper.updateByPrimaryKeySelective(role);
        RoleMenuExample example = new RoleMenuExample();
        RoleMenuExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(role.getId());
        roleMenuMapper.deleteByExample(example);
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
}
