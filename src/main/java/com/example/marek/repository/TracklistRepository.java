
package com.example.marek.repository;

import com.example.marek.entity.Tracklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



import java.util.List;
import java.util.Optional;

@Repository
public interface TracklistRepository extends JpaRepository<Tracklist, Long> {

	// Wszystkie tracklisty użytkownika
	List<Tracklist> findAllByUserId(Long userId);

	// Wyszukiwanie tracklisty po nazwie i właścicielu
	Optional<Tracklist> findByNameAndUserId(String name, Long userId);

	// Sprawdzanie, czy użytkownik posiada tracklistę o danej nazwie
	boolean existsByNameAndUserId(String name, Long userId);

	// Pobieranie tracklisty tylko wtedy, gdy należy do użytkownika
	Optional<Tracklist> findByIdAndUserId(Long tracklistId, Long userId);

	// Sprawdzanie, czy utwór znajduje się na Trackliście
	@Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
			"FROM Tracklist tl JOIN tl.tracks t " +
			"WHERE tl.id = :tracklistId AND t.id = :trackId")
	boolean existsTrack(
			@Param("tracklistId") Long tracklistId,
			@Param("trackId") Long trackId
	);
}
