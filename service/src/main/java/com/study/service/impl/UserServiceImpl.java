package com.study.service.impl;

import com.study.repository.dao.generate.UserMapper;
import com.study.repository.entity.generate.User;
import com.study.repository.entity.generate.UserExample;
import com.study.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public User getName(String name) {
        if(StringUtils.isBlank(name)){
            return  null;
        }
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        criteria.andStateEqualTo(1);
        List<User> usersList = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(usersList)){
            return  null;
        }
        System.out.println(usersList.get(0).getPassword());
        return usersList.get(0);
    }
}
