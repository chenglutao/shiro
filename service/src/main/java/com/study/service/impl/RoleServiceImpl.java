package com.study.service.impl;

import com.study.repository.dao.generate.RoleMapper;
import com.study.repository.entity.generate.Role;
import com.study.repository.entity.generate.RoleExample;
import com.study.service.RoleService;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public List<Role> getRoleByUserId(Integer userId) {
        return roleMapper.getRoleByUserId(userId);
    }
}
