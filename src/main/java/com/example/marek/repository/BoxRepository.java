
package com.example.marek.repository;

import com.example.marek.entity.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {

	List<Box> findAllByUserId(Long userId);

	Optional<Box> findByNameAndUserId(String name, Long userId);

	Optional<Box> findByIdAndUserId(Long boxId, Long userId);

	boolean existsByNameAndUserId(String name, Long userId);
}
