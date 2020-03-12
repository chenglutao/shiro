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

    void add(Role role, List<Integer> menuIds);

    void update(Role role, List<Integer> menuIds);

    RespEntity delete(String roleIds);

    Role detail(String roleId);
}
