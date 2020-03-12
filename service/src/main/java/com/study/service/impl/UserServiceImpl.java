package com.study.service.impl;

import com.study.repository.dao.generate.UserMapper;
import com.study.repository.dao.generate.UserRoleMapper;
import com.study.repository.entity.generate.User;
import com.study.repository.entity.generate.UserExample;
import com.study.repository.entity.generate.UserRole;
import com.study.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-20 13:00
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public User getName(String name) {
        if(StringUtils.isBlank(name)){
            return  null;
        }
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(name);
        criteria.andStateEqualTo(1);
        List<User> usersList = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(usersList)){
            return  null;
        }
        return usersList.get(0);
    }

    @Override
    public List<User> list() {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("id DESC");
        criteria.andStateEqualTo(1);
        List<User> list = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            return  null;
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void add(User user, List<Integer> roleIds) {
        userMapper.insertSelective(user);
        for (Integer roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }
}
