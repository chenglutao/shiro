package com.study.service;

import com.study.repository.entity.generate.Permission;

import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-19 16:56
 */
public interface PermissionService {

    List<Permission> getPermissionByUserId(Integer userId);
}
