
package com.example.marek.controller;

import com.example.marek.entity.User;
import com.example.marek.integration.discogs.DiscogsClient;
import com.example.marek.integration.discogs.DiscogsService;
import com.example.marek.security.CurrentUser;
import com.example.marek.service.AlbumService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {

	private static final String DISCOGS_NEW_RELEASE =
			"https://api.discogs.com/database/search?type=release&q=";

	private static final String DISCOGS_ALBUM =
			"https://api.discogs.com/releases/";

	private static final String DISCOGS_SEARCH =
			"https://api.discogs.com/database/search?type=release&q=";

	private final DiscogsClient discogsClient;
	private final DiscogsService discogsService;
	private final AlbumService albumService;
	private final MessageSource messageSource;

	@Value("${discogs.key}")
	private String discogsKey;

	@Value("${discogs.secret}")
	private String discogsSecret;

	public HomeController(
			DiscogsClient discogsClient,
			DiscogsService discogsService,
			AlbumService albumService,
			MessageSource messageSource) {

		this.discogsClient = discogsClient;
		this.discogsService = discogsService;
		this.albumService = albumService;
		this.messageSource = messageSource;
	}

	private String getDiscogsCredentials() {
		return "&key=" + discogsKey + "&secret=" + discogsSecret;
	}

	@GetMapping("/start")
	public String start(Model model)
			throws IOException, InterruptedException {

		String requestAddress =
				DISCOGS_NEW_RELEASE
						+ LocalDate.now()
						+ getDiscogsCredentials();

		Map<String, Object> response =
				discogsClient.mapRequestData(requestAddress);

		model.addAttribute(
				"thumbs",
				discogsService.thumbsDisplay(response)
		);

		return "home/home";
	}

	@GetMapping("/search")
	public String searchDiscogs(
			@RequestParam String value,
			Model model)
			throws IOException, InterruptedException {

		String encodedValue = URLEncoder.encode(
				value,
				StandardCharsets.UTF_8
		);

		String requestAddress =
				DISCOGS_SEARCH
						+ encodedValue
						+ getDiscogsCredentials();

		Map<String, Object> response =
				discogsClient.mapRequestData(requestAddress);

		model.addAttribute(
				"thumbs",
				discogsService.thumbsDisplay(response)
		);

		return "home/home";
	}

	@GetMapping("/details/{id}")
	public String albumDetails(
			Model model,
			@PathVariable Long id)
			throws IOException, InterruptedException {

		String requestAddress =
				DISCOGS_ALBUM
						+ id
						+ "?"
						+ getDiscogsCredentials().substring(1);

		Map<String, Object> response =
				discogsClient.mapRequestData(requestAddress);

		model.addAttribute(
				"albumDetails",
				discogsService.getAlbumDetails(response)
		);

		model.addAttribute(
				"tracklist",
				discogsService.getTracklist(response)
		);

		model.addAttribute(
				"images",
				discogsService.getImages(response)
		);

		return "home/details";
	}

	@PostMapping("/add/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, String>> addAlbum(
			@PathVariable Long id,
			@AuthenticationPrincipal CurrentUser currentUser) {

		Map<String, String> response = new HashMap<>();

		try {
			User user = currentUser.getUser();

			boolean added = albumService.addAlbumToUser(id, user);

			if (added) {
				response.put("type", "success");
				response.put("message", messageSource.getMessage(
						"album.add.success",
						null,
						LocaleContextHolder.getLocale()
				));
			} else {
				response.put("type", "info");
				response.put("message", messageSource.getMessage(
						"album.add.exists",
						null,
						LocaleContextHolder.getLocale()
				));
			}

			return ResponseEntity.ok(response);

		} catch (IllegalStateException exception) {
			response.put("type", "error");
			response.put("message", messageSource.getMessage(
					"album.add.error",
					null,
					LocaleContextHolder.getLocale()
			));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
