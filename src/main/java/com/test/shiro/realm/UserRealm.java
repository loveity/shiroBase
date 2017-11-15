package com.test.shiro.realm;

import com.test.shiro.EncryptUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.entity.User;
import com.test.service.UserService;

import java.util.HashSet;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> role = new HashSet();
        role.add("user");
        Set<String> pre = new HashSet();
        pre.add("user:list");
        pre.add("user:add");
        pre.add("perm:list");
        pre.add("role:findinfo");
        pre.add("role:corelationperm");
//        role.addAll(userService.findRolesByUserName(userName));
        authorizationInfo.setRoles(role);
//        pre.addAll(userService.findPermissionsByUserName(userName));
        authorizationInfo.setStringPermissions(pre);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        User user;
        try {
            user = userService.getUserByUserName(userName);
        } catch (Exception e) {
            user = new User();
            user.setUserName(userName);
            user.setPassword(EncryptUtils.encryptMD5("111"));
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUserName(),
                user.getPassword(),
                getName());
        return authenticationInfo;
    }

}
