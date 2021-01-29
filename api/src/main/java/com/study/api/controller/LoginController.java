package com.study.api.controller;

import com.study.common.entity.RespEntity;
import com.study.common.util.IpUtils;
import com.study.repository.entity.generate.LoginLog;
import com.study.repository.entity.generate.User;
import com.study.service.LoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenglutao
 */
@Slf4j
@RestController
@Api(value = "登录接口")
public class LoginController {

    @Autowired
    private LoginLogService loginLogService;

    @GetMapping("/test")
    public Object getTest() {
        return RespEntity.ok();
    }

    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/login")
    public Object login(@RequestParam("name") String name,
                        @RequestParam("password") String password,
                        boolean rememberMe, HttpServletRequest request) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(name, password, rememberMe);
            subject.login(token);
            User user = (User) subject.getPrincipal();
            user.setPassword(null);
            // 保存登录日志
            LoginLog loginLog = new LoginLog();
            loginLog.setUserName(user.getUserName());
            String ip = IpUtils.getIpAddress(request);
            loginLog.setIp(ip);
            loginLogService.addLoginLog(loginLog);
            return RespEntity.ok(user);
        } catch (UnknownAccountException e) {
            return RespEntity.error("账号不存在");
        } catch (IncorrectCredentialsException e) {
            return RespEntity.error("账号或密码不正确");
        } catch (LockedAccountException e) {
            return RespEntity.error("账号已被锁定,请联系管理员");
        } catch (AuthenticationException e) {
            return RespEntity.error("账户验证失败");
        } catch (Exception e) {
            return RespEntity.error(e.getMessage());
        }
    }

    @ApiOperation(value = "退出", notes = "退出")
    @GetMapping("/logout")
    public Object logout(){
        SecurityUtils.getSubject().logout();
        return RespEntity.ok();
    }
}
