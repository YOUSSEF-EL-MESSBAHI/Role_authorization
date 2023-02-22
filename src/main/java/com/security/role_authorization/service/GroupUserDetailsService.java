package com.security.role_authorization.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.role_authorization.entity.User;
import com.security.role_authorization.repository.UserRepository;
@Service
public class GroupUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		
		
		return user.map(GroupUserDetails ::new)
				.orElseThrow(() -> new UsernameNotFoundException(username + "doesn't exist "));
	}
	
	
	
	
}
