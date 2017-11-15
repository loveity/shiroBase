package com.test.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.test.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.mapper.PermissionMapper;
import com.test.mapper.RoleMapper;
import com.test.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{



	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private PasswordService passwordService;

	@Override
	public void addUser(User user, Long... roleIds) {
		passwordService.encryptPassword(user);
		userMapper.addUser(user);
		if(roleIds!=null&&roleIds.length>0){
			for(Long roleId:roleIds){
				userMapper.addUserRole(new UserRole(user.getUserId(),roleId));
			}
		}
	}

	@Override
	public void deleteUser(Long userId) {
		userMapper.deleteUserRole(userId);
		userMapper.deleteUser(userId);
	}

	@Override
	public void deleteMoreUsers(Long... userIds) {
		if(userIds!=null&&userIds.length>0){
			for(Long userId:userIds){
				deleteUser(userId);
			}
		}
	}

	@Override
	public User getUserByUserName(String userName) {
		return userMapper.findUserByUserName(userName);
	}

	@Override
	public List<User> getAllUsers() {
		return userMapper.findAllUsers();
	}

	@Override
	public void updateUserRoles(Long userId, Long... roleIds) {
		userMapper.deleteUserRole(userId);
		if(roleIds!=null&&roleIds.length>0){
			for(Long roleId:roleIds){
				userMapper.addUserRole(new UserRole(userId,roleId));
			}
		}
	}

	@Override
	public List<String> findRolesByUserName(String userName) {
		return (userMapper.findRolesByUserName(userName));
	}

	@Override
	public List<String> findPermissionsByUserName(String userName) {
		return (userMapper.findPermissionsByUserName(userName));
	}

	@Override
	public List<Navigation> getNavigationBar(String userName) {
		List<Navigation> navigationBar=new ArrayList<Navigation>();
		Navigation navigation=new Navigation();
		navigation.setNavigationName("admin");
		Permission permission=	new Permission(1L,"user:list","user:list");
		List lis=new ArrayList<Permission>();
		lis.add(permission);
			navigation.setChildNavigations(lis);
			navigationBar.add(navigation);
//		List<Role> roles=roleMapper.findRolesByUserName(userName);
//		for(Role role:roles){
//			navigation=new Navigation();
//			navigation.setNavigationName(role.getRoleDesc());
//			navigation.setChildNavigations(
//					permissionMapper.findNavisByRoleId(role.getRoleId()));
//			navigationBar.add(navigation);
//		}
		return navigationBar;
	}

}
