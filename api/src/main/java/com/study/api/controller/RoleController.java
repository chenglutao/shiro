package com.study.api.controller;

import com.study.api.config.MyShiroRealm;
import com.study.common.entity.RespEntity;
import com.study.repository.entity.generate.Role;
import com.study.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-27 20:21
 */
@Slf4j
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private MyShiroRealm myShiroRealm;

    @GetMapping("list")
    public Object list() {
        try {
            List<Role> list =  roleService.list();
            return RespEntity.ok(list);
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping("add")
    @RequiresPermissions("role:add")
    public Object add(@RequestParam(value = "roleName", required = true) String roleName,
                      @RequestParam(value = "remark", required = false) String remark,
                      @RequestParam(value = "menuIds", required = true)  List<Integer> menuIds){
        try {
            Role role = new Role();
            role.setName(roleName);
            role.setRemark(remark);
            role.setCreateTime(new Date());
            roleService.add(role, menuIds);
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping("update")
    @RequiresPermissions("role:update")
    public Object update(@RequestParam(value = "roleName", required = true) String roleName,
                         @RequestParam(value = "remark", required = false) String remark,
                         @RequestParam(value = "id", required = true) Integer id,
                         @RequestParam(value = "menuIds", required = true)  List<Integer> menuIds){
        try {
            Role role = new Role();
            role.setId(id);
            role.setName(roleName);
            role.setRemark(remark);
            role.setModifyTime(new Date());
            roleService.update(role, menuIds);
            clearCache();
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
            clearCache();
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @GetMapping({"detail"})
    @RequiresPermissions("role:view")
    public Object detail(String roleId){
        try {
            roleService.detail(roleId);
            new MyShiroRealm().clearCache();
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

}
