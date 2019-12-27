package com.study.service;

import com.study.common.entity.RespEntity;
import com.study.repository.entity.generate.Role;

import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-19 16:52
 */
public interface RoleService {

    List<Role> getRoleByUserName(String userName);

    List<Role> list();

    void add(Role role);

    void update(Role role);

    RespEntity delete(String roleIds);
}
