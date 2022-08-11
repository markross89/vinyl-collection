package com.example.marek.album;

import com.example.marek.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface AlbumRepository extends JpaRepository<Album, Long> {
	
	
	Album findAlbumByDiscogsId (long discogsId);
	
	List<Album> findByUsersContains (User user);
	
	@Modifying
	void deleteAlbumByDiscogsId (long id);
	
	@Modifying
	@Query(value = "DELETE FROM album_users WHERE album_id=?1 AND users_id =?2", nativeQuery = true)
	void deleteUserAlbumConstrains (long albumId, long usersId);
	
	
}