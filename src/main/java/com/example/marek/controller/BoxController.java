
package com.example.marek.controller;

import com.example.marek.entity.Album;
import com.example.marek.entity.Box;
import com.example.marek.entity.User;
import com.example.marek.integration.discogs.DiscogsService;
import com.example.marek.security.CurrentUser;
import com.example.marek.service.BoxService;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/box")
public class BoxController {

	private final BoxService boxService;
	private final DiscogsService discogsService;
	private final MessageSource messageSource;

	public BoxController(
			BoxService boxService,
			DiscogsService discogsService,
			MessageSource messageSource
	) {
		this.boxService = boxService;
		this.discogsService = discogsService;
		this.messageSource = messageSource;
	}


	// =====================================================
	// MVC - VIEWS AND REDIRECTS
	// =====================================================


	// -------------------------
	// Display boxes
	// -------------------------

	@GetMapping("/boxes")
	public String displayBoxes(
			Model model,
			@AuthenticationPrincipal CurrentUser customUser
	) {
		User user = customUser.getUser();

		model.addAttribute(
				"boxes",
				boxService.findAllByUserId(user.getId())
		);

		return "box/boxes";
	}


	// -------------------------
	// Create empty box
	// -------------------------

	@GetMapping("/addBox")
	public String createEmptyBoxAndRedirect(
			@RequestParam String name,
			@AuthenticationPrincipal CurrentUser customUser,
			RedirectAttributes redirectAttributes
	) {
		User user = customUser.getUser();
		String boxName = name.trim();

		if (boxName.isEmpty()) {
			addNotification(
					redirectAttributes,
					"box.create.empty",
					"error"
			);

			return "redirect:/box/boxes";
		}

		Optional<Box> createdBox =
				boxService.createEmptyBox(boxName, user);

		if (createdBox.isEmpty()) {
			addNotification(
					redirectAttributes,
					"box.create.duplicate",
					"error"
			);

			return "redirect:/box/boxes";
		}

		addNotification(
				redirectAttributes,
				"box.create.success",
				"success"
		);

		return "redirect:/box/boxes";
	}


	// -------------------------
	// Display box details
	// -------------------------

	@GetMapping("/details/{id}")
	@Transactional(readOnly = true)
	public String displayBoxDetails(
			@PathVariable Long id,
			@AuthenticationPrincipal CurrentUser customUser,
			Model model
	) {
		User user = customUser.getUser();

		Box box = boxService.findByIdAndUserId(id, user.getId())
				.orElseThrow(() ->
						new IllegalArgumentException(
								message("box.notFound")
						)
				);

		model.addAttribute(
				"thumbs",
				discogsService.thumbsDisplayDatabase(
						new ArrayList<>(box.getAlbums())
				)
		);

		model.addAttribute("box", box);

		return "/box/details";
	}


	// -------------------------
	// Remove album from box
	// -------------------------

	@GetMapping("/removeAlbum/{discogsId}/{boxId}")
	public String removeAlbumFromBoxAndRedirect(
			@PathVariable Long discogsId,
			@PathVariable Long boxId,
			@AuthenticationPrincipal CurrentUser customUser
	) {
		User user = customUser.getUser();

		Optional<Boolean> result =
				boxService.removeAlbumFromBox(
						boxId,
						discogsId,
						user.getId()
				);

		if (result.isEmpty()) {
			throw new IllegalArgumentException(
					message("box.album.orBox.notFound")
			);
		}

		return "redirect:/box/details/" + boxId;
	}


	// -------------------------
	// Display add album form
	// -------------------------

	@GetMapping("/addForm/{id}")
	public String displayAddAlbumToBoxForm(
			@PathVariable Long id,
			@AuthenticationPrincipal CurrentUser customUser,
			Model model
	) {
		User user = customUser.getUser();

		Album album = boxService.findAlbumByDiscogsId(id)
				.orElseThrow(() ->
						new IllegalArgumentException(
								message("box.album.notFound")
						)
				);

		model.addAttribute("album", album);

		model.addAttribute(
				"boxes",
				boxService.findAllByUserId(user.getId())
		);

		return "box/addForm";
	}


	// -------------------------
	// Add album to existing box
	// -------------------------

	@GetMapping("/addAlbum/{discogsId}")
	public String addAlbumToBoxAndRedirect(
			@PathVariable Long discogsId,
			@RequestParam Long boxId,
			@AuthenticationPrincipal CurrentUser customUser,
			Model model
	) {
		if (boxId == 0) {
			return "redirect:/box/addForm/" + discogsId;
		}

		User user = customUser.getUser();

		Optional<Boolean> result =
				boxService.addAlbumToBox(
						boxId,
						discogsId,
						user.getId()
				);

		if (result.isEmpty()) {
			throw new IllegalArgumentException(
					message("box.album.orBox.notFound")
			);
		}

		if (!result.get()) {
			model.addAttribute("id", discogsId);
			return "/box/messageAlbum";
		}

		return "redirect:/box/details/" + boxId;
	}


	// -------------------------
	// Create box and add album
	// -------------------------

	@GetMapping("/addCreate/{discogsId}")
	public String createBoxWithAlbumAndRedirect(
			@PathVariable Long discogsId,
			@RequestParam String name,
			@AuthenticationPrincipal CurrentUser customUser,
			Model model
	) {
		User user = customUser.getUser();
		String boxName = name.trim();

		if (boxName.isEmpty()
				|| boxService.existsByNameAndUserId(
				boxName,
				user.getId()
		)) {
			model.addAttribute("id", discogsId);
			return "/box/messageAlbum";
		}

		Album album = boxService.findAlbumByDiscogsId(discogsId)
				.orElseThrow(() ->
						new IllegalArgumentException(
								message("box.album.notFound")
						)
				);

		Optional<Box> createdBox =
				boxService.createBoxWithAlbum(
						boxName,
						user,
						album
				);

		if (createdBox.isEmpty()) {
			model.addAttribute("id", discogsId);
			return "/box/messageAlbum";
		}

		return "redirect:/box/boxes";
	}


	// =====================================================
	// AJAX - API RESPONSES
	// =====================================================


	// -------------------------
	// Delete box
	// -------------------------

	@DeleteMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<Void> ajaxDeleteBox(
			@PathVariable Long id,
			@AuthenticationPrincipal CurrentUser customUser
	) {
		User user = customUser.getUser();

		boolean deleted =
				boxService.deleteBox(id, user.getId());

		if (!deleted) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND,
					message("box.notFound")
			);
		}

		return ResponseEntity.noContent().build();
	}


	// -------------------------
	// Get user's boxes for album
	// -------------------------

	@GetMapping("/api/albums/{albumId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ajaxGetBoxesForAlbum(
			@PathVariable Long albumId,
			@AuthenticationPrincipal CurrentUser customUser
	) {
		User user = customUser.getUser();

		Album album = boxService.findAlbumByDiscogsId(albumId)
				.orElseThrow(() ->
						new ResponseStatusException(
								HttpStatus.NOT_FOUND,
								message("box.album.notFound")
						)
				);

		List<Map<String, Object>> boxes =
				boxService.getBoxesForAlbum(
						album,
						user.getId()
				);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("boxes", boxes);

		return ResponseEntity.ok(response);
	}


	// -------------------------
	// Add album to existing box
	// -------------------------

	@PostMapping("/api/{boxId}/albums/{albumId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ajaxAddAlbumToBox(
			@PathVariable Long boxId,
			@PathVariable Long albumId,
			@AuthenticationPrincipal CurrentUser customUser
	) {
		User user = customUser.getUser();

		Optional<Boolean> result =
				boxService.addAlbumToBox(
						boxId,
						albumId,
						user.getId()
				);

		if (result.isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND,
					message("box.album.orBox.notFound")
			);
		}

		Map<String, Object> response = new LinkedHashMap<>();

		if (!result.get()) {
			response.put("type", "info");
			response.put(
					"message",
					message("box.album.add.exists")
			);

			return ResponseEntity.ok(response);
		}

		response.put("type", "success");
		response.put(
				"message",
				message("box.album.add.success")
		);
		response.put("containsAlbum", true);

		return ResponseEntity.ok(response);
	}


	// -------------------------
	// Remove album from box
	// -------------------------

	@DeleteMapping("/api/{boxId}/albums/{albumId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ajaxRemoveAlbumFromBox(
			@PathVariable Long boxId,
			@PathVariable Long albumId,
			@AuthenticationPrincipal CurrentUser customUser
	) {
		User user = customUser.getUser();

		Optional<Boolean> result =
				boxService.removeAlbumFromBox(
						boxId,
						albumId,
						user.getId()
				);

		if (result.isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND,
					message("box.album.orBox.notFound")
			);
		}

		Map<String, Object> response = new LinkedHashMap<>();

		if (!result.get()) {
			response.put("type", "info");
			response.put(
					"message",
					message("box.album.remove.notFound")
			);

			return ResponseEntity.ok(response);
		}

		response.put("type", "success");
		response.put(
				"message",
				message("box.album.remove.success")
		);
		response.put("containsAlbum", false);

		return ResponseEntity.ok(response);
	}


	// -------------------------
	// Create box with album
	// -------------------------

	@PostMapping("/api/create")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ajaxCreateBoxWithAlbum(
			@RequestParam String name,
			@RequestParam Long albumId,
			@AuthenticationPrincipal CurrentUser customUser
	) {
		User user = customUser.getUser();
		String boxName = name.trim();

		Map<String, Object> response = new LinkedHashMap<>();

		if (boxName.isEmpty()) {
			response.put("type", "error");
			response.put(
					"message",
					message("box.create.empty")
			);

			return ResponseEntity.badRequest().body(response);
		}

		if (boxService.existsByNameAndUserId(
				boxName,
				user.getId()
		)) {
			response.put("type", "error");
			response.put(
					"message",
					message("box.create.duplicate")
			);

			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body(response);
		}

		Album album = boxService.findAlbumByDiscogsId(albumId)
				.orElseThrow(() ->
						new ResponseStatusException(
								HttpStatus.NOT_FOUND,
								message("box.album.notFound")
						)
				);

		Optional<Box> createdBox =
				boxService.createBoxWithAlbum(
						boxName,
						user,
						album
				);

		if (createdBox.isEmpty()) {
			response.put("type", "error");
			response.put(
					"message",
					message("box.create.duplicate")
			);

			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body(response);
		}

		Box box = createdBox.get();

		response.put("type", "success");
		response.put(
				"message",
				message("box.create.withAlbum.success")
		);
		response.put("id", box.getId());
		response.put("name", box.getName());
		response.put("containsAlbum", true);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(response);
	}


	// -------------------------
	// Create empty box
	// -------------------------

	@PostMapping("/api/create-empty")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ajaxCreateEmptyBox(
			@RequestParam String name,
			@AuthenticationPrincipal CurrentUser customUser
	) {
		User user = customUser.getUser();
		String boxName = name.trim();

		Map<String, Object> response = new LinkedHashMap<>();

		if (boxName.isEmpty()) {
			response.put("type", "error");
			response.put(
					"message",
					message("box.create.empty")
			);
			response.put("messageKey", "box.create.empty");

			return ResponseEntity.badRequest().body(response);
		}

		Optional<Box> createdBox =
				boxService.createEmptyBox(boxName, user);

		if (createdBox.isEmpty()) {
			response.put("type", "error");
			response.put(
					"message",
					message("box.create.duplicate")
			);
			response.put("messageKey", "box.create.duplicate");

			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body(response);
		}

		Box box = createdBox.get();

		response.put("type", "success");
		response.put(
				"message",
				message("box.create.success")
		);
		response.put("messageKey", "box.create.success");
		response.put("id", box.getId());
		response.put("name", box.getName());

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(response);
	}


	// =====================================================
	// PRIVATE HELPERS
	// =====================================================

	private String message(String code) {
		return messageSource.getMessage(
				code,
				null,
				LocaleContextHolder.getLocale()
		);
	}

	private void addNotification(
			RedirectAttributes redirectAttributes,
			String message,
			String type
	) {
		redirectAttributes.addFlashAttribute(
				"notificationMessage",
				message
		);

		redirectAttributes.addFlashAttribute(
				"notificationType",
				type
		);
	}
}
