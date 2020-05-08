package com.study.service;

import com.study.repository.entity.generate.Permission;

import java.util.List;

/**
 * @author chenglutao
 */
public interface PermissionService {

    List<Permission> getPermissionByUserId(Integer userId);
}
