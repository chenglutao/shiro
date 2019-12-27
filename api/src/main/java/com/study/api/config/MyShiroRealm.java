package com.study.api.config;

import com.study.repository.entity.generate.Menu;
import com.study.repository.entity.generate.Role;
import com.study.repository.entity.generate.User;
import com.study.service.MenuService;
import com.study.service.RoleService;
import com.study.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chenglutao
 * @date 2019-12-19 14:56
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        try {
            // 获取用户角色集
            List<Role> roleList = roleService.getRoleByUserName(user.getUserName());
            Set<String> roleSet = roleList.stream().map(Role::getName).collect(Collectors.toSet());
            simpleAuthorizationInfo.addRoles(roleSet);
            // 获取用户权限集
            List<Menu> permissionList = menuService.getUserPermissions(user.getUserName());
            Set<String> permissionSet = permissionList.stream().map(Menu::getPerms).collect(Collectors.toSet());
            simpleAuthorizationInfo.setStringPermissions(permissionSet);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        User user = userService.getName(userName);
        if (user == null) {
            throw new UnknownAccountException();
        }
        if (user.getState() == 0) {
            throw new LockedAccountException();
        }
        ByteSource salt = ByteSource.Util.bytes(user.getUserName());
        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
    }

    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

    public static void main(String[] args) {
        String result = new SimpleHash("MD5", "123456", ByteSource.Util.bytes("admin"), 1024).toHex();
        System.out.println(result);
    }
}
