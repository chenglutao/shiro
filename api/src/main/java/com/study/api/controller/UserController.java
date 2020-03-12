package com.study.api.controller;

import com.study.api.config.MyShiroRealm;
import com.study.common.entity.RespEntity;
import com.study.repository.entity.generate.User;
import com.study.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
public class UserController {

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
                      @RequestParam(value = "roleIds", required = true) List<Integer> roleIds){
        try {
            User user = user(userName, email, phone, remark);
            user.setPassword(MyShiroRealm.simpleHash(userName, password));
            user.setCreatTime(new Date());
            user.setState(1);
            userService.add(user, roleIds);
            return RespEntity.ok();
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
                         @RequestParam(value = "state", required = false) Integer state){
        try {
            User user = user(userName, email, phone, remark);
            user.setId(id);
            user.setModifyTime(new Date());
            user.setState(state);
            userService.update(user);
            new MyShiroRealm().clearCache();
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
