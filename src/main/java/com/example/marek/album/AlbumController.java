package com.example.marek.album;


import com.example.marek.box.BoxRepository;
import com.example.marek.homeHttp.ApiService;
import com.example.marek.tracklist.TracklistRepository;
import com.example.marek.user.CurrentUser;
import com.example.marek.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Transactional
@Controller
@RequestMapping("/album")
public class AlbumController {
	
	private final AlbumRepository albumRepository;
	private final TracklistRepository tracklistRepository;
	private final BoxRepository boxRepository;
	private final ApiService apiService;
	
	public AlbumController (AlbumRepository albumRepository, TracklistRepository tracklistRepository,
							BoxRepository boxRepository, ApiService apiService) {
		
		this.albumRepository = albumRepository;
		this.tracklistRepository = tracklistRepository;
		this.boxRepository = boxRepository;
		this.apiService = apiService;
	}
	
	@GetMapping("/albums")
	public String display (Model model, @AuthenticationPrincipal CurrentUser customUser) throws JsonProcessingException {
		
		List<Album> albums = albumRepository.findByUsersContains(customUser.getUser());
		model.addAttribute("thumbs", apiService.thumbsDisplayDatabase(albums));
		
		return "album/albums";
	}
	
	@GetMapping("/remove/{id}")
	public String remove (@PathVariable long id, @AuthenticationPrincipal CurrentUser customUser) {
		
		Album album = albumRepository.findAlbumByDiscogsId(id);
		tracklistRepository.deleteTrackTracklistConstrains(album.getId(), customUser.getUser().getId());
		boxRepository.deleteBoxAlbumConstrainsWithUserId(customUser.getUser().getId(), album.getId());
		albumRepository.deleteUserAlbumConstrains(album.getId(), customUser.getUser().getId());
		List<User> users = album.getUsers();
		if (users.isEmpty()) {
			albumRepository.deleteAlbumByDiscogsId(id);
		}
		return "redirect:/album/albums";
	}
	
	@GetMapping("/details/{id}")
	public String albumDetails (Model model, @PathVariable long id) {
		
		model.addAttribute("album", albumRepository.findAlbumByDiscogsId(id));
		return "album/details";
	}
	
}
