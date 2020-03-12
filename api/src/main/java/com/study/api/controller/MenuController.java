package com.study.api.controller;

import com.study.api.config.MyShiroRealm;
import com.study.common.util.SpringContextUtils;
import com.study.common.entity.Key;
import com.study.common.entity.RespEntity;
import com.study.repository.entity.generate.Menu;
import com.study.repository.entity.generate.User;
import com.study.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-27 18:01
 */
@Slf4j
@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @GetMapping("{userName}")
    public Object getUserMenus(@PathVariable String userName){
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            if (!StringUtils.equalsIgnoreCase(userName, user.getUserName())){
                return RespEntity.error("您无权获取别人的菜单");
            }
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
    public Object add(@RequestParam(value = "parentId", required = true) Integer parentId,
                      @RequestParam(value = "menuName", required = true) String menuName,
                      @RequestParam(value = "url", required = false) String url,
                      @RequestParam(value = "perms", required = true) String perms,
                      @RequestParam(value = "icon", required = false) String icon,
                      @RequestParam(value = "type", required = true) String type,
                      @RequestParam(value = "orderBy", required = true) Integer orderBy){
        try {
            Menu menu = menu(parentId, menuName, url, perms, icon, type, orderBy);
            menu.setCreateTime(new Date());
            menuService.add(menu);
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }
    @PostMapping("update")
    @RequiresPermissions("menu:update")
    public Object update(@RequestParam(value = "parentId", required = true) Integer parentId,
                         @RequestParam(value = "menuName", required = true) String menuName,
                         @RequestParam(value = "url", required = false) String url,
                         @RequestParam(value = "perms", required = true) String perms,
                         @RequestParam(value = "icon", required = false) String icon,
                         @RequestParam(value = "type", required = true) String type,
                         @RequestParam(value = "orderBy", required = true) Integer orderBy,
                         @RequestParam(value = "id", required = true) Integer id) {
        try {
            Menu menu = menu(parentId, menuName, url, perms, icon, type, orderBy);
            menu.setModifyTime(new Date());
            menu.setId(id);
            menuService.update(menu);
            clearCache();
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
            if (Key.ERROR.getCode().equals(key.getCode())){
                return RespEntity.error(respEntity.getMsg());
            }
            clearCache();
            return RespEntity.ok(respEntity);
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    private Menu menu(Integer parentId, String menuName, String url, String perms, String icon, String type, Integer orderBy) {
        Menu menu = new Menu();
        menu.setParentId(parentId);
        menu.setMenuName(menuName);
        menu.setUrl(url);
        menu.setPerms(perms);
        menu.setType(type);
        menu.setIcon(icon);
        menu.setOrderBy(orderBy);
        return menu;
    }
}
