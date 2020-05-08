package com.study.service;

import com.study.common.entity.PageEntity;
import com.study.common.entity.RespEntity;
import com.study.repository.entity.generate.Role;

import java.util.List;

/**
 * @author chenglutao
 */
public interface RoleService {

    List<Role> getRoleByUserName(String userName);

    List<Role> list();

    void add(Role role, String menuIds);

    void update(Role role, String menuIds);

    RespEntity delete(String roleIds);

    Role detail(String roleId);

    PageEntity<Role> page(String roleName, String pageSize, String pageNum);
}
