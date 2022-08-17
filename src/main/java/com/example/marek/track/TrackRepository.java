package com.example.marek.track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
	
	@Query(value = "select * from track join tracklist_tracks on track.id=tracks_id where tracklist_id=?1", nativeQuery = true)
	List<Track> findTracksFromTracklist (long id);
	
	@Query(value = "select * from track t join album_tracks a on t.id=a.tracks_id join album_users u on a.album_id=u.album_id  where "+
			"u.users_id=?1", nativeQuery = true)
	List<Track> findTracksByUser (long id);
	
	@Modifying
	@Query(value = "DELETE FROM tracklist_tracks WHERE tracklist_id=?1 AND tracks_id =?2", nativeQuery = true)
	void deleteTrackTracklistConstrains (long tracklistId, long trackId);
	
	
	// search query for collection
	@Query(value = "select * from track t join album_tracks a on t.id= a.tracks_id join album_users u on a.album_id=u.album_id where u.users_id =?1 " +
			"and title like concat('%',?2,'%') or u.users_id =?1 and artist like concat('%',?2,'%') or u.users_id =?1 and album like concat('%',?2," +
			"'%') or u.users_id =?1 and label like concat('%',?2,'%')", nativeQuery = true)
	List<Track> searchSongCollection (long userId, String query);
	
	// search query for track list
	@Query(value = "select * from track join tracklist_tracks on track.id=tracks_id where tracklist_id=?1 and title like concat('%',?2,'%') or " +
			"tracklist_id=?1 and artist like concat('%',?2,'%') or tracklist_id=?1 and album like concat('%',?2,'%') or tracklist_id=?1 and label " +
			"like concat('%',?2,'%')", nativeQuery = true)
	List<Track> searchTracklist (long tracklistId, String query);
	
	
	//  track lists sorting queries
	
	@Query(value = "select * from track join tracklist_tracks on track.id=tracks_id where tracklist_id=?1 ORDER BY artist", nativeQuery = true)
	List<Track> sortByArtist (long id);
	
	@Query(value = "select * from track join tracklist_tracks on track.id=tracks_id where tracklist_id=?1 ORDER BY title", nativeQuery = true)
	List<Track> sortByTitle (long id);
	
	@Query(value = "select * from track join tracklist_tracks on track.id=tracks_id where tracklist_id=?1 ORDER BY album", nativeQuery = true)
	List<Track> sortByAlbum (long id);
	
	@Query(value = "select * from track join tracklist_tracks on track.id=tracks_id where tracklist_id=?1 ORDER BY label", nativeQuery = true)
	List<Track> sortByLabel (long id);
	
	@Query(value = "select * from track join tracklist_tracks on track.id=tracks_id where tracklist_id=?1 ORDER BY date", nativeQuery = true)
	List<Track> sortByDate (long id);
	
	@Query(value = "select * from track join tracklist_tracks on track.id=tracks_id where tracklist_id=?1 ORDER BY Duration", nativeQuery = true)
	List<Track> sortByDuration (long id);
	
	
	//  song collection sorting queries
	
	@Query(value = "select * from track t join album_tracks a on t.id= a.tracks_id join album_users u on a.album_id=u.album_id where u.users_id "+
			"=?1 order by title", nativeQuery = true)
	List<Track> sortAllUserSongsByTitle (long id);
	
	@Query(value = "select * from track t join album_tracks a on t.id= a.tracks_id join album_users u on a.album_id=u.album_id where u.users_id "+
			"=?1 order by artist", nativeQuery = true)
	List<Track> sortAllUserSongsByArtist (long id);
	
	@Query(value = "select * from track t join album_tracks a on t.id= a.tracks_id join album_users u on a.album_id=u.album_id where u.users_id "+
			"=?1 order by album", nativeQuery = true)
	List<Track> sortAllUserSongsByAlbum (long id);
	
	@Query(value = "select * from track t join album_tracks a on t.id= a.tracks_id join album_users u on a.album_id=u.album_id where u.users_id "+
			"=?1 order by label", nativeQuery = true)
	List<Track> sortAllUserSongsByLabel (long id);
	
	@Query(value = "select * from track t join album_tracks a on t.id= a.tracks_id join album_users u on a.album_id=u.album_id where u.users_id "+
			"=?1 order by date", nativeQuery = true)
	List<Track> sortAllUserSongsByDate (long id);
	
	@Query(value = "select * from track t join album_tracks a on t.id= a.tracks_id join album_users u on a.album_id=u.album_id where u.users_id "+
			"=?1 order by duration", nativeQuery = true)
	List<Track> sortAllUserSongsByDuration (long id);
	
	
}
