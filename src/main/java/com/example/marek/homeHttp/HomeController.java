package com.example.marek.homeHttp;

import com.example.marek.album.Album;
import com.example.marek.album.AlbumRepository;
import com.example.marek.image.Image;

import com.example.marek.track.Track;

import com.example.marek.user.CurrentUser;
import com.example.marek.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@Controller
@RequestMapping("/home")
public class HomeController {
	
	
	private static final String DISCOGS_NEW_RELASE = "https://api.discogs.com/database/search?type=release&q=";
	private static final String DISCOGS_KEY_SECRET = "key=RqgTChKtuwVLyeWqVgFL&secret=sTkBKTNnXoSjWCnVRIOabYoBZUsDgzmY";
	private static final String DISCOGS_ALBUM = "https://api.discogs.com/releases/";
	private static final String DISCOGS_SEARCH = "https://api.discogs.com/database/search?type=release&q=";
	
	private final ApiController apiController;
	private final AlbumRepository albumRepository;
	
	
	public HomeController (ApiController apiController, AlbumRepository albumRepository) {
		
		this.apiController = apiController;
		this.albumRepository = albumRepository;
		
	}
	
	@GetMapping("/start") // home page
	public String start (Model model) throws JsonProcessingException {
		
		Map map = apiController.mapRequestData(String.join("", DISCOGS_NEW_RELASE, java.time.LocalDate.now().toString(), "&", DISCOGS_KEY_SECRET));
		model.addAttribute("thumbs", apiController.thumbsDisplay(map));
		return "home/home";
	}
	
	@GetMapping("/search")  // search Discogs library
	public String searchDiscogs (@RequestParam String value, Model model) throws JsonProcessingException {
		
		Map map = apiController.mapRequestData(String.join("", DISCOGS_SEARCH, value.replaceAll(" ", ","), "&", DISCOGS_KEY_SECRET));
		model.addAttribute("thumbs", apiController.thumbsDisplay(map));
		return "home/home";
	}
	
	@GetMapping("/details/{id}")  // display details of album found on discogs
	public String albumDetails (Model model, @PathVariable long id) throws JsonProcessingException {
		
		Map map = apiController.mapRequestData(String.join("", DISCOGS_ALBUM, String.valueOf(id), "?", DISCOGS_KEY_SECRET));
		model.addAttribute("albumDetails", apiController.getAlbumDetails(map));
		model.addAttribute("tracklist", apiController.getTracklist(map));
		model.addAttribute("images", apiController.getImages(map));
		return "home/details";
	}
	
	@GetMapping("/add/{id}")  // adds album to database / constraint to logged user
	public String addAlbum (@PathVariable long id, @AuthenticationPrincipal CurrentUser customUser) throws JsonProcessingException {
		
		Album a = albumRepository.findAlbumByDiscogsId(id);
		if (a != null) {
			List<User> users = a.getUsers();
			for (User u : users) {
				if (u.getId().equals(customUser.getUser().getId())) {
					return "/album/messageAlbum";
				}
			}
			users.add(customUser.getUser());
			a.setUsers(users);
			albumRepository.save(a);
		}
		else {
			Map map = apiController.mapRequestData(String.join("", DISCOGS_ALBUM, String.valueOf(id), "?", DISCOGS_KEY_SECRET));
			
			List<Track> tracks = apiController.saveAlbumTracks(id, map);
			List<Image> images = apiController.saveAlbumImages(map);
			apiController.saveAlbum(images, tracks, map, customUser.getUser(), id);
		}
		return "redirect:/album/albums";
	}
}
