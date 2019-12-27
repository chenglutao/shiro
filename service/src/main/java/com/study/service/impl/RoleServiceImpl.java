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
    public void add(Role role) {
        roleMapper.insertSelective(role);
    }

    @Override
    public void update(Role role) {
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public RespEntity delete(String roleId) {
        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(Integer.valueOf(roleId));
        List<UserRole> userRoleList = userRoleMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(userRoleList))
            return RespEntity.error("角色删除失败,角色下存在用户数据!");
        roleMapper.deleteByPrimaryKey(Integer.valueOf(roleId));
        RoleMenuExample roleMenuExample = new RoleMenuExample();
        RoleMenuExample.Criteria cr = roleMenuExample.createCriteria();
        cr.andRoleIdEqualTo(Integer.valueOf(roleId));
        roleMenuMapper.deleteByExample(roleMenuExample);
        return RespEntity.ok();
    }
}
