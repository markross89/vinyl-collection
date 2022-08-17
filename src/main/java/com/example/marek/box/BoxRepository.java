package com.example.marek.box;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {
	
	List<Box> findAllByUserId(long userId);
	
	Optional<Box> findByName(String name);
	
	@Modifying
	@Query(value = "DELETE FROM box_albums WHERE box_id=?1 AND albums_id =?2", nativeQuery = true)
	void deleteBoxAlbumConstrainsWithBoxId (long boxId, long albumId);
	
	@Modifying
	@Query(value = "delete b from box_albums b join album_users u on b.albums_id=u.album_id where u.users_id =?1 and b.albums_id =?2", nativeQuery = true)
	void deleteBoxAlbumConstrainsWithUserId (long userId, long albumId);
	

}
