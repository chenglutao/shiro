package com.study.api.config;

import com.study.repository.entity.generate.Permission;
import com.study.repository.entity.generate.Role;
import com.study.repository.entity.generate.User;
import com.study.service.PermissionService;
import com.study.service.RoleService;
import com.study.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    private PermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        try {
            List<Role> roleList = roleService.getRoleByUserId(user.getId());
            for (Role role : roleList) {
                authorizationInfo.addRole(role.getName());
            }
            List<Permission> permissionList = permissionService.getPermissionByUserId(user.getId());
            for (Permission permission : permissionList) {
                authorizationInfo.addStringPermission(permission.getName());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = userService.getName(username);
        if (user == null) {
            throw new UnknownAccountException();
        }
        if (user.getState() == 0) {
            throw new LockedAccountException();
        }
        ByteSource salt = ByteSource.Util.bytes(user.getName());
        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
    }

    public static void main(String[] args) {
        String result = new SimpleHash("MD5", "123456", ByteSource.Util.bytes("chenglutao"), 1024).toHex();
        System.out.println(result);
    }
}
