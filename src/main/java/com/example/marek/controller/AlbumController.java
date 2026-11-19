
package com.example.marek.controller;

import com.example.marek.entity.Album;
import com.example.marek.entity.User;
import com.example.marek.security.CurrentUser;
import com.example.marek.service.AlbumService;
import com.example.marek.integration.discogs.DiscogsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Controller
@Transactional
@RequestMapping("/album")
public class AlbumController {

	private final AlbumService albumService;
	private final DiscogsService discogsService;

	public AlbumController(
			AlbumService albumService,
			DiscogsService discogsService) {
		this.albumService = albumService;
		this.discogsService = discogsService;
	}

	@GetMapping("/albums")
	public String display(
			Model model,
			@AuthenticationPrincipal CurrentUser customUser) {

		User user = customUser.getUser();

		List<Album> albums = albumService.findAlbumsByUser(user);

		model.addAttribute("albums", albums);

		return "album/albums";
	}

	@DeleteMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<Void> removeAlbumFromCollection(
			@PathVariable Long id,
			@AuthenticationPrincipal CurrentUser customUser) {

		if (customUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		albumService.removeAlbumFromUser(id, customUser.getUser());

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/details/{id}")
	public String albumDetails(
			Model model,
			@PathVariable Long id,
			@RequestParam(required = false) String returnUrl,
			@AuthenticationPrincipal CurrentUser customUser) {

		Album album = albumService.findByDiscogsId(id);

		boolean isInCollection = false;

		if (customUser != null) {
			isInCollection = albumService.isAlbumInCollection(
					album.getDiscogsId(),
					customUser.getUser().getId()
			);
		}

		model.addAttribute("album", album);
		model.addAttribute("isInCollection", isInCollection);

		String backUrl = "/album/albums";

		if (returnUrl != null &&
				(returnUrl.matches("^/tracklist/\\d+$") ||
						returnUrl.matches("^/box/details/\\d+$") ||
						"/track/tracks".equals(returnUrl))) {

			backUrl = returnUrl;
		}

		model.addAttribute("backUrl", backUrl);

		return "album/details";
	}
}
