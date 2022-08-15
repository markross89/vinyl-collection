package com.example.marek.homeHttp;

import com.example.marek.album.Album;
import com.example.marek.album.AlbumRepository;
import com.example.marek.image.Image;
import com.example.marek.image.ImageRepository;
import com.example.marek.track.Track;
import com.example.marek.track.TrackRepository;
import com.example.marek.user.CurrentUser;
import com.example.marek.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


@Controller
@RequestMapping("/home")
public class HomeController {
	
	
	private static final String DISCOGS_NEW_RELASE = "https://api.discogs.com/database/search?type=release&q=";
	private static final String DISCOGS_KEY_SECRET = "key=RqgTChKtuwVLyeWqVgFL&secret=sTkBKTNnXoSjWCnVRIOabYoBZUsDgzmY";
	private static final String DISCOGS_ALBUM = "https://api.discogs.com/releases/";
	private static final String DISCOGS_SEARCH = "https://api.discogs.com/database/search?type=release&q=";
	
	private final ApiController apiController;
	private final TrackRepository trackRepository;
	private final ImageRepository imageRepository;
	private final AlbumRepository albumRepository;
	
	
	public HomeController (ApiController apiController, TrackRepository trackRepository,
						   ImageRepository imageRepository, AlbumRepository albumRepository) {
		
		this.apiController = apiController;
		this.trackRepository = trackRepository;
		this.imageRepository = imageRepository;
		this.albumRepository = albumRepository;
		
	}
	
	
	@GetMapping("/start")
	public String start (Model model) throws JsonProcessingException {
		
		Map map = apiController.mapRequestData(String.join("", DISCOGS_NEW_RELASE, java.time.LocalDate.now().toString(), "&", DISCOGS_KEY_SECRET));
		model.addAttribute("thumbs", apiController.thumbsDisplay(map));
		return "home/home";
	}
	
	@GetMapping("/search")
	public String searchDiscogs (@RequestParam String value, Model model) throws JsonProcessingException {
		
		Map map = apiController.mapRequestData(String.join("", DISCOGS_SEARCH, value.replaceAll(" ", ","), "&", DISCOGS_KEY_SECRET));
		model.addAttribute("thumbs", apiController.thumbsDisplay(map));
		
		
		return "home/home";
		
	}
	
	@GetMapping("/details/{id}")
	public String albumDetails (Model model, @PathVariable long id) throws JsonProcessingException {
		
		Map map = apiController.mapRequestData(String.join("", DISCOGS_ALBUM, String.valueOf(id), "?", DISCOGS_KEY_SECRET));
		model.addAttribute("albumDetails", apiController.getAlbumDetails(map));
		model.addAttribute("tracklist", apiController.getTracklist(map));
		model.addAttribute("images", apiController.getImages(map));
		
		return "home/details";
	}
	
	@GetMapping("/add/{id}")
	public String addAlbum (@PathVariable long id, @AuthenticationPrincipal CurrentUser customUser) throws JsonProcessingException {
		
		Album a = albumRepository.findAlbumByDiscogsId(id);
		
		if (a != null) {
			List<User> users = a.getUsers();
			users.add(customUser.getUser());
			a.setUsers(users);
			albumRepository.save(a);
			
		}
		else {
			
			Map map = apiController.mapRequestData(String.join("", DISCOGS_ALBUM, String.valueOf(id), "?", DISCOGS_KEY_SECRET));
			
			LocalDate date = LocalDate.now();
			
			List<Track> tracks = new ArrayList<>();
			for (Object o : apiController.getAlbumTracklist(map)) {
				Track track = Track.builder().position(apiController.getTracklistSongDetail(o, "position"))
						.title(apiController.getTracklistSongDetail(o, "title"))
						.duration(apiController.getTracklistSongDetail(o, "duration"))
						.album(apiController.getAlbumTitle(map))
						.artist(apiController.getAlbumArtist(map))
						.label(apiController.getAlbumLabel(map))
						.discogsId(id)
						.date(Date.valueOf(date))
						.build();
				tracks.add(track);
				trackRepository.save(track);
			}
			
			List<Image> images = new ArrayList<>();
			for (Object o : apiController.getAlbumImages(map)) {
				Image image = Image.builder().type(apiController.getAlbumImageDetail(o, "type"))
						.uri(apiController.getAlbumImageDetail(o, "uri")).build();
				images.add(image);
				imageRepository.save(image);
			}
			
			
			List<User> usersList = new ArrayList<>();
			usersList.add(customUser.getUser());
			Album album = Album.builder().discogsId(id)
					.artist(apiController.getAlbumArtist(map))
					.title(apiController.getAlbumTitle(map))
					.label(apiController.getAlbumLabel(map))
					.catno(apiController.getAlbumCatno(map))
					.uri(apiController.getAlbumUri(map))
					.genre(apiController.getAlbumGenre(map))
					.images(images)
					.tracks(tracks)
					.date(Date.valueOf(date))
					.users(usersList).build();
			albumRepository.save(album);
			
		}
		return "redirect:/album/albums";
		
		
	}
	
	
}
