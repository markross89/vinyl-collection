
package com.example.marek.controller;

import com.example.marek.entity.Album;
import com.example.marek.entity.PlaybackType;
import com.example.marek.entity.Tracklist;
import com.example.marek.security.CurrentUser;
import com.example.marek.service.BoxService;
import com.example.marek.service.TracklistService;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/tracklist")
public class TracklistController {

	private final TracklistService tracklistService;
	private final BoxService boxService;
	private final MessageSource messageSource;

	public TracklistController(
			TracklistService tracklistService,
			BoxService boxService,
			MessageSource messageSource
	) {
		this.tracklistService = tracklistService;
		this.boxService = boxService;
		this.messageSource = messageSource;
	}

	// --------------------------------------------------
	// TRACKLIST LIST
	// --------------------------------------------------

	@GetMapping("/tracklists")
	public String getUserTracklists(
			@AuthenticationPrincipal CurrentUser currentUser,
			Model model) {

		Long userId = currentUser.getUser().getId();

		model.addAttribute(
				"tracklists",
				tracklistService.findAllByUserId(userId)
		);

		return "tracklist/tracklists";
	}

	// --------------------------------------------------
	// TRACKLIST DETAILS
	// --------------------------------------------------

	@GetMapping("/{tracklistId}")
	public String getTracklistDetails(
			@PathVariable Long tracklistId,
			@AuthenticationPrincipal CurrentUser currentUser,
			Model model) {

		Long userId = currentUser.getUser().getId();

		Tracklist tracklist = tracklistService
				.findByIdAndUserId(tracklistId, userId)
				.orElseThrow(() ->
						new ResponseStatusException(HttpStatus.NOT_FOUND)
				);

		model.addAttribute("tracklist", tracklist);

		model.addAttribute(
				"tracks",
				tracklistService.findTracksByTracklistIdAndUserId(
						tracklistId, userId
				)
		);

		model.addAttribute("activeSort", "manual");

		model.addAttribute(
				"playbackTime",
				tracklistService.calculatePlaybackTime(tracklistId, userId)
		);

		return "tracklist/details";
	}

	// --------------------------------------------------
	// CREATE TRACKLIST
	// --------------------------------------------------

	@PostMapping("/api")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> createTracklist(
			@RequestParam String name,
			@AuthenticationPrincipal CurrentUser currentUser,
			Locale locale) {

		Map<String, Object> response = new LinkedHashMap<>();

		if (name == null || name.trim().isEmpty()) {

			response.put(
					"message",
					messageSource.getMessage(
							"tracklist.create.name.empty",
							null,
							locale
					)
			);

			return ResponseEntity.badRequest().body(response);
		}

		return tracklistService.createTracklist(
						name.trim(),
						currentUser.getUser()
				)
				.map(tracklist -> {

					response.put("id", tracklist.getId());
					response.put("name", tracklist.getName());

					response.put(
							"message",
							messageSource.getMessage(
									"tracklist.create.success",
									null,
									locale
							)
					);

					return ResponseEntity
							.status(HttpStatus.CREATED)
							.body(response);
				})
				.orElseGet(() -> {

					response.put(
							"message",
							messageSource.getMessage(
									"tracklist.create.name.exists",
									null,
									locale
							)
					);

					return ResponseEntity
							.status(HttpStatus.CONFLICT)
							.body(response);
				});
	}

	// --------------------------------------------------
	// CREATE TRACKLIST WITH TRACK
	// --------------------------------------------------

	@PostMapping("/api/create-with-track")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> createTracklistWithTrack(
			@RequestParam String name,
			@RequestParam Long trackId,
			@AuthenticationPrincipal CurrentUser currentUser,
			Locale locale) {

		Map<String, Object> response = new LinkedHashMap<>();

		if (name == null || name.trim().isEmpty()) {

			response.put(
					"message",
					messageSource.getMessage(
							"tracklist.create.name.empty",
							null,
							locale
					)
			);

			return ResponseEntity.badRequest().body(response);
		}

		if (trackId == null) {

			response.put(
					"message",
					messageSource.getMessage(
							"tracklist.createWithTrack.error",
							null,
							locale
					)
			);

			return ResponseEntity.badRequest().body(response);
		}

		return tracklistService.createTracklistWithTrack(
						name.trim(),
						trackId,
						currentUser.getUser()
				)
				.map(tracklist -> {

					response.put("id", tracklist.getId());
					response.put("name", tracklist.getName());
					response.put("trackId", trackId);

					response.put(
							"message",
							messageSource.getMessage(
									"tracklist.createWithTrack.success",
									null,
									locale
							)
					);

					return ResponseEntity
							.status(HttpStatus.CREATED)
							.body(response);
				})
				.orElseGet(() -> {

					response.put(
							"message",
							messageSource.getMessage(
									"tracklist.createWithTrack.error",
									null,
									locale
							)
					);

					return ResponseEntity
							.status(HttpStatus.CONFLICT)
							.body(response);
				});
	}

	// --------------------------------------------------
	// CREATE BOX FROM TRACKLIST
	// --------------------------------------------------

	@PostMapping("/api/{tracklistId}/create-box")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> createBoxFromTracklist(
			@PathVariable Long tracklistId,
			@RequestParam String name,
			@AuthenticationPrincipal CurrentUser currentUser,
			Locale locale) {

		Map<String, Object> response = new LinkedHashMap<>();

		Long userId = currentUser.getUser().getId();

		if (name == null || name.trim().isEmpty()) {

			response.put(
					"message",
					messageSource.getMessage(
							"tracklist.createBox.name.empty",
							null,
							locale
					)
			);

			return ResponseEntity.badRequest().body(response);
		}

		if (tracklistService.findByIdAndUserId(
				tracklistId, userId
		).isEmpty()) {

			response.put(
					"message",
					messageSource.getMessage(
							"tracklist.createBox.error",
							null,
							locale
					)
			);

			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(response);
		}

		List<Album> albums =
				tracklistService.findAlbumsByTracklistIdAndUserId(
						tracklistId, userId
				);

		return boxService.createBoxWithAlbums(
						name.trim(),
						currentUser.getUser(),
						albums
				)
				.map(box -> {

					response.put("id", box.getId());
					response.put("name", box.getName());

					response.put(
							"message",
							messageSource.getMessage(
									"tracklist.createBox.success",
									null,
									locale
							)
					);

					return ResponseEntity
							.status(HttpStatus.CREATED)
							.body(response);
				})
				.orElseGet(() -> {

					response.put(
							"message",
							messageSource.getMessage(
									"tracklist.createBox.name.exists",
									null,
									locale
							)
					);

					return ResponseEntity
							.status(HttpStatus.CONFLICT)
							.body(response);
				});
	}

	// --------------------------------------------------
	// DELETE TRACKLIST
	// --------------------------------------------------

	@DeleteMapping("/api/{tracklistId}")
	@ResponseBody
	public ResponseEntity<Void> deleteTracklist(
			@PathVariable Long tracklistId,
			@AuthenticationPrincipal CurrentUser currentUser) {

		Long userId = currentUser.getUser().getId();

		boolean deleted = tracklistService.deleteTracklist(
				tracklistId, userId
		);

		if (!deleted) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.noContent().build();
	}

	// --------------------------------------------------
	// REMOVE TRACK FROM TRACKLIST
	// --------------------------------------------------

	@DeleteMapping("/api/{tracklistId}/tracks/{trackId}")
	@ResponseBody
	public ResponseEntity<Void> removeTrackFromTracklist(
			@PathVariable Long tracklistId,
			@PathVariable Long trackId,
			@AuthenticationPrincipal CurrentUser currentUser) {

		boolean removed = tracklistService.removeTrackFromTracklist(
				tracklistId,
				trackId,
				currentUser.getUser().getId()
		);

		if (!removed) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.noContent().build();
	}

	// --------------------------------------------------
	// CLEAR TRACKLIST
	// --------------------------------------------------

	@DeleteMapping("/api/{tracklistId}/tracks")
	@ResponseBody
	public ResponseEntity<Void> clearTracklist(
			@PathVariable Long tracklistId,
			@AuthenticationPrincipal CurrentUser currentUser) {

		boolean cleared = tracklistService.clearTracklist(
				tracklistId,
				currentUser.getUser().getId()
		);

		if (!cleared) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.noContent().build();
	}

	// --------------------------------------------------
	// REORDER TRACKS
	// --------------------------------------------------

	@PostMapping("/api/{tracklistId}/tracks/reorder")
	@ResponseBody
	public ResponseEntity<Void> reorderTracks(
			@PathVariable Long tracklistId,
			@RequestBody List<Long> trackIds,
			@AuthenticationPrincipal CurrentUser currentUser) {

		boolean reordered = tracklistService.reorderTracks(
				tracklistId,
				currentUser.getUser().getId(),
				trackIds
		);

		if (!reordered) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.noContent().build();
	}

	// --------------------------------------------------
	// SEARCH TRACKS IN TRACKLIST
	// --------------------------------------------------

	@GetMapping("/{tracklistId}/tracks/search")
	public String searchTracks(
			@PathVariable Long tracklistId,
			@RequestParam(defaultValue = "") String query,
			@AuthenticationPrincipal CurrentUser currentUser,
			Model model) {

		Long userId = currentUser.getUser().getId();

		Tracklist tracklist = tracklistService
				.findByIdAndUserId(tracklistId, userId)
				.orElseThrow(() ->
						new ResponseStatusException(HttpStatus.NOT_FOUND)
				);

		model.addAttribute("tracklist", tracklist);

		model.addAttribute(
				"tracks",
				tracklistService.searchTracksInTracklist(
						tracklistId, userId, query.trim()
				)
		);

		model.addAttribute(
				"playbackTime",
				tracklistService.calculatePlaybackTime(tracklistId, userId)
		);

		return "tracklist/details";
	}

	// --------------------------------------------------
	// SORT TRACKS IN TRACKLIST
	// --------------------------------------------------

	@GetMapping("/{tracklistId}/tracks")
	public String sortTracks(
			@PathVariable Long tracklistId,
			@RequestParam(defaultValue = "title") String sort,
			@AuthenticationPrincipal CurrentUser currentUser,
			Model model) {

		Long userId = currentUser.getUser().getId();

		Tracklist tracklist = tracklistService
				.findByIdAndUserId(tracklistId, userId)
				.orElseThrow(() ->
						new ResponseStatusException(HttpStatus.NOT_FOUND)
				);

		model.addAttribute("tracklist", tracklist);

		model.addAttribute(
				"tracks",
				tracklistService.sortTracksInTracklist(
						tracklistId, userId, sort
				)
		);

		model.addAttribute(
				"playbackTime",
				tracklistService.calculatePlaybackTime(tracklistId, userId)
		);

		model.addAttribute("activeSort", sort);

		return "tracklist/details";
	}

	// --------------------------------------------------
	// UPDATE PLAYBACK SETTINGS
	// --------------------------------------------------

	@PostMapping("/api/{tracklistId}/playback-settings")
	@ResponseBody
	public ResponseEntity<Void> updatePlaybackSettings(
			@PathVariable Long tracklistId,
			@RequestParam PlaybackType playbackType,
			@RequestParam Integer transitionDuration,
			@AuthenticationPrincipal CurrentUser currentUser) {

		boolean updated = tracklistService.updatePlaybackSettings(
				tracklistId,
				currentUser.getUser().getId(),
				playbackType,
				transitionDuration
		);

		if (!updated) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.noContent().build();
	}
}
