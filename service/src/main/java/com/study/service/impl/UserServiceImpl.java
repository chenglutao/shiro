package com.study.service.impl;

import com.study.common.entity.RespEntity;
import com.study.repository.dao.generate.UserMapper;
import com.study.repository.dao.generate.UserRoleMapper;
import com.study.repository.entity.generate.User;
import com.study.repository.entity.generate.UserExample;
import com.study.repository.entity.generate.UserRole;
import com.study.repository.entity.generate.UserRoleExample;
import com.study.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
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
    public RespEntity add(User user, String roleIds) {
        if (verify(user.getUserName())){
            return RespEntity.error("用户名已存在");
        }
        userMapper.insertSelective(user);
        addUserRole(user.getId(), roleIds);
        return RespEntity.ok();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void update(User user, String roleIds) {
        userMapper.updateByPrimaryKeySelective(user);
        //更新关联角色
        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(user.getId());
        userRoleMapper.deleteByExample(example);
        addUserRole(user.getId(), roleIds);
    }

    @Override
    public User detail(String userId) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(Integer.valueOf(userId));
        criteria.andStateEqualTo(1);
        List<User> list = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            return  null;
        }
        return list.get(0);
    }

    @Override
    public void delete(String userIds) {
        String[] user = userIds.split(",");
        for (String userId : user) {
            userMapper.deleteByPrimaryKey(Integer.valueOf(userId));
            UserRoleExample example = new UserRoleExample();
            UserRoleExample.Criteria criteria = example.createCriteria();
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
            userRoleMapper.deleteByExample(example);
        }
    }

    @Override
    public void passwordUpdate(Integer userId, String newPassword) {
        User user = new User();
        user.setModifyTime(new Date());
        user.setPassword(newPassword);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void passwordReset(String userName, String password) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userName);

        User user = new User();
        user.setPassword(password);
        user.setModifyTime(new Date());
        userMapper.updateByExampleSelective(user, example);
    }

    public void addUserRole(Integer userId, String roleIds){
        String[] role = roleIds.split(",");
        Arrays.stream(role).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Integer.valueOf(roleId));
            userRoleMapper.insert(userRole);
        });
    }

    private boolean verify(String userName) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(userName)) {
            criteria.andUserNameEqualTo(userName);
        }
        criteria.andStateEqualTo(1);
        int count = userMapper.countByExample(example);
        if (count > 0) {
            return true;
        }
        return false;
    }
}
