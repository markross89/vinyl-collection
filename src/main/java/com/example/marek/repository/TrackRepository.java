
package com.example.marek.repository;

import com.example.marek.entity.Track;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
	// GENRES

	// Find distinct genres from user's collection
	@Query("SELECT DISTINCT a.genre FROM Album a " +
			"JOIN a.users u " +
			"WHERE u.id = :userId " +
			"AND a.genre IS NOT NULL " +
			"AND a.genre <> '' " +
			"ORDER BY a.genre")
	List<String> findGenresByUser(
			@Param("userId") Long userId);

	// =====================================================
	// TRACKLIST
	// =====================================================

	@Query("SELECT t FROM Tracklist tl JOIN tl.tracks t " +
			"WHERE tl.id = :tracklistId")
	List<Track> findTracksFromTracklist(
			@Param("tracklistId") Long tracklistId,
			Sort sort
	);

	default List<Track> findTracksFromTracklist(Long tracklistId) {
		return findTracksFromTracklist(tracklistId, Sort.unsorted());
	}

	// =====================================================
	// USER COLLECTION
	// =====================================================

	@Query("SELECT DISTINCT t FROM Track t " +
			"JOIN t.release a " +
			"JOIN a.users u " +
			"WHERE u.id = :userId")
	List<Track> findTracksByUser(
			@Param("userId") Long userId,
			Sort sort
	);

	default List<Track> findTracksByUser(Long userId) {
		return findTracksByUser(userId, Sort.unsorted());
	}

	@Query("SELECT t FROM Track t " +
			"JOIN t.release a " +
			"JOIN a.users u " +
			"WHERE t.id = :trackId " +
			"AND u.id = :userId")
	Optional<Track> findByIdAndUserId(
			@Param("trackId") Long trackId,
			@Param("userId") Long userId
	);
	// =====================================================
	// SEARCH
	// =====================================================

	@Query("SELECT DISTINCT t FROM Track t " +
			"JOIN t.release a " +
			"JOIN a.users u " +
			"WHERE u.id = :userId " +
			"AND (" +
			"LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(t.artist) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(t.album) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(t.label) LIKE LOWER(CONCAT('%', :query, '%'))" +
			")")
	List<Track> searchSongCollection(
			@Param("userId") Long userId,
			@Param("query") String query,
			Sort sort
	);

	default List<Track> searchSongCollection(Long userId, String query) {
		return searchSongCollection(userId, query, Sort.unsorted());
	}

	@Query("SELECT DISTINCT t FROM Tracklist tl " +
			"JOIN tl.tracks t " +
			"WHERE tl.id = :tracklistId " +
			"AND (" +
			"LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(t.artist) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(t.album) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(t.label) LIKE LOWER(CONCAT('%', :query, '%'))" +
			")")
	List<Track> searchTracklist(
			@Param("tracklistId") Long tracklistId,
			@Param("query") String query,
			Sort sort
	);

	default List<Track> searchTracklist(Long tracklistId, String query) {
		return searchTracklist(tracklistId, query, Sort.unsorted());
	}


	// FILTER BY GENRE
	@Query("SELECT DISTINCT t FROM Track t " +
			"JOIN t.release a " +
			"JOIN a.users u " +
			"WHERE u.id = :userId " +
			"AND LOWER(a.genre) = LOWER(:genre)")
	List<Track> findTracksByUserAndGenre(
			@Param("userId") Long userId,
			@Param("genre") String genre);

	// =====================================================
	// TRACKLIST SORTING
	// =====================================================

	default List<Track> sortByArtist(Long id) {
		return findTracksFromTracklist(id, Sort.by("artist"));
	}

	default List<Track> sortByTitle(Long id) {
		return findTracksFromTracklist(id, Sort.by("title"));
	}

	default List<Track> sortByAlbum(Long id) {
		return findTracksFromTracklist(id, Sort.by("album"));
	}

	default List<Track> sortByLabel(Long id) {
		return findTracksFromTracklist(id, Sort.by("label"));
	}

	default List<Track> sortByDate(Long id) {
		return findTracksFromTracklist(id, Sort.by("date"));
	}

	default List<Track> sortByDuration(Long id) {
		return findTracksFromTracklist(id, Sort.by("duration"));
	}

	// =====================================================
	// USER COLLECTION SORTING
	// =====================================================

	default List<Track> sortAllUserSongsByTitle(Long id) {
		return findTracksByUser(id, Sort.by("title"));
	}

	default List<Track> sortAllUserSongsByArtist(Long id) {
		return findTracksByUser(id, Sort.by("artist"));
	}

	default List<Track> sortAllUserSongsByAlbum(Long id) {
		return findTracksByUser(id, Sort.by("album"));
	}

	default List<Track> sortAllUserSongsByLabel(Long id) {
		return findTracksByUser(id, Sort.by("label"));
	}

	default List<Track> sortAllUserSongsByDate(Long id) {
		return findTracksByUser(id, Sort.by("date"));
	}

	default List<Track> sortAllUserSongsByDuration(Long id) {
		return findTracksByUser(id, Sort.by("duration"));
	}
}
