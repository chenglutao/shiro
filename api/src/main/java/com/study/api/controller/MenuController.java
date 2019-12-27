package com.study.api.controller;

import com.study.api.config.MyShiroRealm;
import com.study.common.entity.Key;
import com.study.common.entity.RespEntity;
import com.study.common.exception.BusinessException;
import com.study.repository.entity.generate.Menu;
import com.study.repository.entity.generate.User;
import com.study.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-27 18:01
 */
@Slf4j
@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("{userName}")
    public Object getUserMenus(@PathVariable String userName){
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            if (!StringUtils.equalsIgnoreCase(userName, user.getUserName()))
                return RespEntity.error("您无权获取别人的菜单");
            List<Menu> list = menuService.getUserMenus(userName);
            return RespEntity.ok(list);
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @GetMapping("tree")
    public Object list(){
        try {
            List<Menu> list =  menuService.list();
            return RespEntity.ok(list);
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping("add")
    @RequiresPermissions("menu:add")
    public Object add(Menu menu){
        try {
            menuService.add(menu);
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }
    @PostMapping("update")
    @RequiresPermissions("menu:update")
    public Object update(Menu menu){
        try {
            menuService.update(menu);
            new MyShiroRealm().clearCache();
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping("delete/{menuId}")
    @RequiresPermissions("menu:delete")
    public Object delete(@PathVariable String menuId){
        try {
            RespEntity respEntity = menuService.delete(menuId);
            Key key = respEntity.getKey();
            if (Key.ERROR.getCode().equals(key.getCode()))
                return RespEntity.error(respEntity.getMsg());
            new MyShiroRealm().clearCache();
            return RespEntity.ok(respEntity);
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

}
