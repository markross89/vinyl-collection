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
	
	@Modifying
	@Query(value = "DELETE FROM tracklist_tracks WHERE tracklist_id=?1 AND tracks_id =?2", nativeQuery = true)
	void deleteTrackTracklistConstrains (long tracklistId, long trackId);
	
	
	


}
