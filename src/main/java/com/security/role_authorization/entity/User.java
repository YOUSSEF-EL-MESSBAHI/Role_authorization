package com.security.role_authorization.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "User_Auth")
public class User {
	@Id
	@GeneratedValue
	 private int id ; 
	 private String username;
	 private String password;
	 private String roles;
	 private Boolean active ;
}
