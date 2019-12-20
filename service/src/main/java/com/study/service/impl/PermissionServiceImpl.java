package com.study.service.impl;

import com.study.repository.dao.generate.PermissionMapper;
import com.study.repository.entity.generate.Permission;
import com.study.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-19 17:07
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getPermissionByUserId(Integer userId) {
        return permissionMapper.getPermissionByUserId(userId);
    }
}
