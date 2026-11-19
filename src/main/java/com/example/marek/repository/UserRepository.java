
package com.example.marek.repository;

import com.example.marek.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// Wyszukiwanie użytkownika po adresie e-mail
	Optional<User> findByUsername(String username);

	// Sprawdzanie, czy użytkownik już istnieje
	boolean existsByUsername(String username);
}
