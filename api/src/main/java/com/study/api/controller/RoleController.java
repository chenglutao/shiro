package com.study.api.controller;

import com.study.api.config.MyShiroRealm;
import com.study.common.entity.RespEntity;
import com.study.repository.entity.generate.Role;
import com.study.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-27 20:21
 */
@Slf4j
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("list")
    public Object list(){
        try {
            List<Role> list =  roleService.list();
            return RespEntity.ok(list);
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping("add")
    @RequiresPermissions("role:add")
    public Object add(Role role){
        try {
            roleService.add(role);
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping("update")
    @RequiresPermissions("role:update")
    public Object update(Role role){
        try {
            roleService.update(role);
            new MyShiroRealm().clearCache();
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping({"delete/roleIds"})
    @RequiresPermissions("role:delete")
    public Object delete(String roleIds){
        try {
            roleService.delete(roleIds);
            new MyShiroRealm().clearCache();
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

}
