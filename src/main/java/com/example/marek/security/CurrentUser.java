package com.example.marek.security;


import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
public class CurrentUser extends User {
	
	private final com.example.marek.entity.User user;
	
	public CurrentUser(String username, String password,
					   Collection<? extends GrantedAuthority> authorities,
					   com.example.marek.entity.User user) {
		super(username, password, authorities);
		this.user = user;
	}
	public com.example.marek.entity.User getUser() {return user;}
}