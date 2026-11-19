
package com.example.marek.repository;

import com.example.marek.entity.Album;
import com.example.marek.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

	Optional<Album> findByDiscogsId(Long discogsId);

	List<Album> findDistinctByUsersContains(User user);

	boolean existsByDiscogsIdAndUsers_Id(Long discogsId, Long userId);
}
