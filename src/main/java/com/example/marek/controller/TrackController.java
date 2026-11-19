
package com.example.marek.controller;

import com.example.marek.entity.User;
import com.example.marek.repository.TrackRepository;
import com.example.marek.security.CurrentUser;
import com.example.marek.service.TracklistService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/track")
public class TrackController {

	private final TrackRepository trackRepository;
	private final TracklistService tracklistService;

	public TrackController(
			TrackRepository trackRepository,
			TracklistService tracklistService) {

		this.trackRepository = trackRepository;
		this.tracklistService = tracklistService;
	}

	// --------------------------------------------------
	// TRACK COLLECTION
	// --------------------------------------------------

	@GetMapping("/tracks")
	public String display(
			Model model,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		model.addAttribute(
				"tracks",
				trackRepository.findTracksByUser(user.getId())
		);

		prepareTracksView(model, user, null, null);

		return "track/tracks";
	}

	// --------------------------------------------------
	// TRACK SEARCH
	// --------------------------------------------------

	@GetMapping("/searchCollection")
	public String searchCollection(
			@RequestParam String query,
			@AuthenticationPrincipal CurrentUser customUser,
			Model model) {

		User user = customUser.getUser();

		model.addAttribute(
				"tracks",
				trackRepository.searchSongCollection(user.getId(), query)
		);

		model.addAttribute("query", query);

		prepareTracksView(model, user, null, null);

		return "track/tracks";
	}

	// --------------------------------------------------
	// TRACK SORTING
	// --------------------------------------------------

	@GetMapping("/sortBy/title")
	public String sortByTitle(
			Model model,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		model.addAttribute(
				"tracks",
				trackRepository.sortAllUserSongsByTitle(user.getId())
		);

		prepareTracksView(model, user, "title", null);

		return "track/tracks";
	}

	@GetMapping("/sortBy/artist")
	public String sortByArtist(
			Model model,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		model.addAttribute(
				"tracks",
				trackRepository.sortAllUserSongsByArtist(user.getId())
		);

		prepareTracksView(model, user, "artist", null);

		return "track/tracks";
	}

	@GetMapping("/sortBy/album")
	public String sortByAlbum(
			Model model,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		model.addAttribute(
				"tracks",
				trackRepository.sortAllUserSongsByAlbum(user.getId())
		);

		prepareTracksView(model, user, "album", null);

		return "track/tracks";
	}

	@GetMapping("/sortBy/label")
	public String sortByLabel(
			Model model,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		model.addAttribute(
				"tracks",
				trackRepository.sortAllUserSongsByLabel(user.getId())
		);

		prepareTracksView(model, user, "label", null);

		return "track/tracks";
	}

	@GetMapping("/sortBy/date")
	public String sortByDate(
			Model model,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		model.addAttribute(
				"tracks",
				trackRepository.sortAllUserSongsByDate(user.getId())
		);

		prepareTracksView(model, user, "date", null);

		return "track/tracks";
	}

	@GetMapping("/sortBy/duration")
	public String sortByDuration(
			Model model,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		model.addAttribute(
				"tracks",
				trackRepository.sortAllUserSongsByDuration(user.getId())
		);

		prepareTracksView(model, user, "duration", null);

		return "track/tracks";
	}

	// --------------------------------------------------
	// TRACK FILTERING
	// --------------------------------------------------

	@GetMapping("/filterByGenre")
	public String filterByGenre(
			@RequestParam(required = false) String genre,
			@AuthenticationPrincipal CurrentUser customUser,
			Model model) {

		User user = customUser.getUser();

		if (genre == null || genre.trim().isEmpty()) {

			model.addAttribute(
					"tracks",
					trackRepository.findTracksByUser(user.getId())
			);

		} else {

			model.addAttribute(
					"tracks",
					trackRepository.findTracksByUserAndGenre(
							user.getId(),
							genre
					)
			);
		}

		prepareTracksView(model, user, null, genre);

		return "track/tracks";
	}

	// --------------------------------------------------
	// TRACKLIST RETRIEVAL FOR SELECTED TRACK
	// --------------------------------------------------

	@GetMapping("/api/tracks/{trackId}/tracklists")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getTracklistsForTrack(
			@PathVariable Long trackId,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		if (trackRepository.findByIdAndUserId(
				trackId,
				user.getId()
		).isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		List<Map<String, Object>> result = tracklistService
				.findAllByUserId(user.getId())
				.stream()
				.map(tracklist -> {

					Map<String, Object> item = new HashMap<>();

					item.put("id", tracklist.getId());
					item.put("name", tracklist.getName());

					boolean containsTrack = tracklist.getTracks()
							.stream()
							.anyMatch(track ->
									trackId.equals(track.getId()));

					item.put("containsTrack", containsTrack);

					return item;
				})
				.collect(Collectors.toList());

		return ResponseEntity.ok(result);
	}

	// --------------------------------------------------
	// ADD TRACK TO EXISTING TRACKLIST
	// --------------------------------------------------

	@PostMapping("/api/tracklists/{tracklistId}/tracks/{trackId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addTrackToTracklist(
			@PathVariable Long trackId,
			@PathVariable Long tracklistId,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		Map<String, Object> result = new HashMap<>();

		boolean added = tracklistService.addTrackToTracklist(
				tracklistId,
				trackId,
				user.getId()
		);

		if (added) {

			result.put("added", true);
			result.put("message", "Track added to tracklist.");

			return ResponseEntity.ok(result);
		}

		result.put("added", false);
		result.put("message", "Tracklist or track not found, or track already exists.");

		return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
	}

	// --------------------------------------------------
	// COMMON VIEW DATA
	// --------------------------------------------------

	private void prepareTracksView(
			Model model,
			User user,
			String activeSort,
			String selectedGenre) {

		model.addAttribute(
				"genres",
				trackRepository.findGenresByUser(user.getId())
		);

		model.addAttribute("activeSort", activeSort);

		model.addAttribute(
				"selectedGenre",
				selectedGenre == null ? "" : selectedGenre
		);
	}
}
