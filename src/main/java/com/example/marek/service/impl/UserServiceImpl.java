
package com.example.marek.service.impl;

import com.example.marek.entity.Album;
import com.example.marek.entity.Role;
import com.example.marek.entity.User;
import com.example.marek.repository.RoleRepository;
import com.example.marek.repository.UserRepository;
import com.example.marek.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final String DEFAULT_ROLE = "ROLE_USER";

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	public UserServiceImpl(
			UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public void saveUser(User user) {

		Role userRole = roleRepository.findByName(DEFAULT_ROLE)
				.orElseThrow(() -> new IllegalStateException(
						"Role " + DEFAULT_ROLE + " does not exist in database"
				));

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled(true);

		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		user.setRoles(roles);

		userRepository.save(user);
	}

	@Override
	public void updateUser(User user, String newPassword) {

		if (newPassword != null && !newPassword.trim().isEmpty()) {
			user.setPassword(passwordEncoder.encode(newPassword));
		}

		userRepository.save(user);
	}

	@Override
	public void deleteUser(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalStateException("User not found"));

		for (Album album : user.getAlbums()) {
			album.getUsers().remove(user);
		}

		user.getAlbums().clear();

		userRepository.delete(user);
	}
}
