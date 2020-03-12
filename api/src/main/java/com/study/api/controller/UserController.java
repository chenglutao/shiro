package com.study.api.controller;

import com.study.api.config.MyShiroRealm;
import com.study.common.entity.RespEntity;
import com.study.repository.entity.generate.User;
import com.study.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-20 9:46
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @GetMapping("list")
    public Object list(){
        try {
            List<User> list =  userService.list();
            return RespEntity.ok(list);
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping("add")
    @RequiresPermissions("user:add")
    public Object add(@RequestParam(value = "userName", required = true) String userName,
                      @RequestParam(value = "password", required = true) String password,
                      @RequestParam(value = "email", required = false) String email,
                      @RequestParam(value = "phone", required = false) String phone,
                      @RequestParam(value = "remark", required = false) String remark,
                      @RequestParam(value = "roleIds", required = true) String roleIds){
        try {

            User user = user(userName, email, phone, remark);
            user.setPassword(MyShiroRealm.simpleHash(userName, password));
            user.setCreatTime(new Date());
            user.setState(1);
            return userService.add(user, roleIds);
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping("update")
    @RequiresPermissions("user:update")
    public Object update(@RequestParam(value = "userName", required = true) String userName,
                         @RequestParam(value = "email", required = false) String email,
                         @RequestParam(value = "phone", required = false) String phone,
                         @RequestParam(value = "remark", required = false) String remark,
                         @RequestParam(value = "id", required = true) Integer id,
                         @RequestParam(value = "state", required = false) Integer state,
                         @RequestParam(value = "roleIds", required = true) String roleIds){
        try {
            User user = user(userName, email, phone, remark);
            user.setId(id);
            user.setModifyTime(new Date());
            user.setState(state);
            userService.update(user, roleIds);
            User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (StringUtils.equalsIgnoreCase(currentUser.getUserName(), userName)) {
                clearCache();
            }
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping({"detail"})
    @RequiresPermissions("user:detail")
    public Object detail(@RequestParam(value = "userId", required = true) String userId) {
        try {
            User user = userService.detail(userId);
            return RespEntity.ok(user);
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping({"delete/userIds"})
    @RequiresPermissions("user:delete")
    public Object delete(@PathVariable String userIds){
        try {
            userService.delete(userIds);
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping({"password/update"})
    @RequiresPermissions("user:update")
    public Object passwordUpdate(@RequestParam(value = "oldPassword", required = true) String oldPassword,
                                 @RequestParam(value = "newPassword", required = true) String newPassword){
        try {
            User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!StringUtils.equals(currentUser.getPassword(), MyShiroRealm.simpleHash(currentUser.getUserName(), oldPassword))) {
                return RespEntity.error("原密码不正确");
            }
            userService.passwordUpdate(currentUser.getId(), MyShiroRealm.simpleHash(currentUser.getUserName(), newPassword));
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @PostMapping({"password/reset"})
    @RequiresPermissions("user:reset")
    public Object passwordReset(@RequestParam(value = "userName", required = true) String userName){
        try {
            userService.passwordReset(userName, MyShiroRealm.simpleHash(userName, "test123456"));
            return RespEntity.ok();
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }



    private User user(String userName, String email, String phone, String remark) {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRemark(remark);
        return user;
    }

}
