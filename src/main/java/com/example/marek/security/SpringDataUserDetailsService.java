
package com.example.marek.security;

import com.example.marek.entity.User;
import com.example.marek.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SpringDataUserDetailsService implements UserDetailsService {

	private final UserService userService;

	public SpringDataUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		User user = userService.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(
						"User not found: " + username
				));

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		user.getRoles().forEach(role ->
				grantedAuthorities.add(
						new SimpleGrantedAuthority(role.getName())
				)
		);

		return new CurrentUser(
				user.getUsername(),
				user.getPassword(),
				grantedAuthorities,
				user
		);
	}
}
