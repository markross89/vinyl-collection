package com.example.marek.tracklist;

import com.example.marek.box.Box;
import com.example.marek.track.Track;
import com.example.marek.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TracklistRepository  extends JpaRepository<Tracklist, Long> {
	
	List<Tracklist> findAllByUserId(long userId);
	
	Optional<Tracklist> findByNameAndUser(String name, User user);
	
	
	@Modifying
	@Query(value = "delete t from tracklist_tracks t join album_tracks a on t.tracks_id=a.tracks_id join tracklist s on t.tracklist_id=s.id where a" +
			".album_id=?1 and s.user_id=?2", nativeQuery = true)
	void deleteTrackTracklistConstrains (long albumId, long userId);

}
