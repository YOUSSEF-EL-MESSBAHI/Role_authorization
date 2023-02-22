package com.security.role_authorization.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.security.role_authorization.entity.User;

public class GroupUserDetails implements UserDetails {
	
	private String username;
	private String password;
	private Boolean isactive;
	private List<GrantedAuthority> authorities; 
	
	public GroupUserDetails(User user) {
		this.username=user.getUsername();
		this.password=user.getPassword();
		this.isactive=user.getActive();
		this.authorities=Arrays.stream(user.getRoles()
				.split(",")).map(SimpleGrantedAuthority ::new)
				.collect(Collectors.toList());
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isactive;
	}

}
