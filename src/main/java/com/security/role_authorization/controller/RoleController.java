package com.security.role_authorization.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.role_authorization.entity.User;
import com.security.role_authorization.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class RoleController {
	
	private static final String DEFAULT_ROLE="ROLE_USER";
	private static final String[] ADMIN_ACCESS={"ROLE_ADMIN","ROLE_MODERATOR"};
	private static final String[] MODERATOR_ACCESS={"ROLE_MODERATOR"};
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@PostMapping("/join")
	public String joinGroup(@RequestBody User user) {
		user.setRoles(DEFAULT_ROLE);
		String encryptedPWD =bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPWD);
		userRepository.save(user);
		return "Hi"+user.getUsername() + "into the group";
	}
	// if loggin user is ADMIN -> ADMIN + MODERATOR
	// if loggin user is MODERATOR -> MODERATOR
	
	@GetMapping("/access/{user_id}/{user_role}")
	@Secured("ROLE_ADMIN")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String giveAcessToUser(@PathVariable int user_id,@PathVariable String user_role,Principal principal) {
		User user=userRepository.findById(user_id).get();
		List<String> activeRoles=getRolesByLoggedinUser(principal);
		String newRoles="";
		if(activeRoles.contains(user_role)) {
			newRoles =user.getRoles()+","+user_role;
			user.setRoles(newRoles);
		}
		userRepository.save(user);
		return"Hi"+user.getUsername()+"new role assign to you by"+principal.getName();
	}
	
	@GetMapping("/loadUsers")
	@Secured("ROLE_ADMIN")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<User> loadUsers(){
		return userRepository.findAll();
	}
	
	@Secured("ROLE_USER")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping
	public String testUserAccess(){
		return "user can only access this";
	}
	
	
	private List<String> getRolesByLoggedinUser(Principal principal){
		String roles=getUserLoggedin(principal).getRoles();
		List<String> assignRoles=Arrays.stream(roles.split(",")).collect(Collectors.toList()) ;
		if (assignRoles.contains("ROLE_ADMIN")){
			return Arrays.stream(ADMIN_ACCESS).collect(Collectors.toList());
		}
		if (assignRoles.contains("ROLE_MODERATOR")){
			return Arrays.stream(MODERATOR_ACCESS).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}
	
	private User getUserLoggedin(Principal principal) {
		return userRepository.findByUsername(principal.getName()).get();
	}
}
