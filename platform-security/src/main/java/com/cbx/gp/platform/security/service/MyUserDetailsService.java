package com.cbx.gp.platform.security.service;

import java.util.ArrayList;
import java.util.List;

import com.cbx.gp.platform.security.dao.UserPermissionMapper;
import com.cbx.gp.platform.security.model.Role;
import com.cbx.gp.platform.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService{
	@Autowired
	private UserPermissionMapper userPermissionMapper;
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		if(username==null){
			throw new UsernameNotFoundException("账号为null");
		}
		User user=userPermissionMapper.getUserByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("账号不存在或密码错误");
		}
		List<Role> roles=userPermissionMapper.getRoleListByUserId(user.getId());
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		for(Role role:roles) {
			//之所以要添加"ROLE_"是因为前端的thymeleaf-extras-springsecurity5校验的时候，
			//sec:authorize="hasRole('TEACHER')",只有在这里加ROLE_才能识别TEACHER
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			//authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
		}


		/*
		UserDetails userDetails = userCache.getUserFromCache(workNo);
		if(userDetails==null)
			userDetails=new org.springframework.security.core.userdetails.User(user.getWorkNo(),user.getPassword(),authorities);
		userCache.putUserInCache(userDetails);
		*/
		user.setAuthorities(authorities);
		return user;
	}


	
}
