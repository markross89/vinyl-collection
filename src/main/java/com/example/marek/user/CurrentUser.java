package com.example.marek.user;


import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
public class CurrentUser extends User {
	
	private final com.example.marek.user.User user;
	
	public CurrentUser(String username, String password,
					   Collection<? extends GrantedAuthority> authorities,
					   com.example.marek.user.User user) {
		super(username, password, authorities);
		this.user = user;
	}
	public com.example.marek.user.User getUser() {return user;}
}