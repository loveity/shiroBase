package com.test.service;

import java.util.List;
import java.util.Set;

import com.test.entity.Navigation;
import com.test.entity.User;

public interface UserService {
    void addUser(User user, Long... roleIds);

    void deleteUser(Long userId);

    void deleteMoreUsers(Long... userIds);

    User getUserByUserName(String userName);

    List<User> getAllUsers();

    void updateUserRoles(Long userId, Long... roleIds);

    List<String> findRolesByUserName(String userName);

    List<String> findPermissionsByUserName(String userName);

    List<Navigation> getNavigationBar(String userName);
}
