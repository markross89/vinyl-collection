
package com.example.marek.service;

import com.example.marek.entity.User;

import java.util.Optional;

public interface UserService {

	Optional<User> findByUsername(String username);

	void saveUser(User user);

	void updateUser(User user, String newPassword);

	void deleteUser(Long userId);
}
