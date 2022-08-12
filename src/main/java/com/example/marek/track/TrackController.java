package com.example.marek.track;

import com.example.marek.album.AlbumRepository;
import com.example.marek.image.ImageRepository;
import com.example.marek.tracklist.Tracklist;
import com.example.marek.tracklist.TracklistRepository;
import com.example.marek.user.CurrentUser;
import com.example.marek.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Controller
@RequestMapping("/track")
public class TrackController {
	
	private final AlbumRepository albumRepository;
	private final ImageRepository imageRepository;
	private final TrackRepository trackRepository;
	private final UserRepository userRepository;
	private final TracklistRepository tracklistRepository;
	
	public TrackController (AlbumRepository albumRepository, ImageRepository imageRepository, TrackRepository trackRepository,
							UserRepository userRepository, TracklistRepository tracklistRepository) {
		
		this.albumRepository = albumRepository;
		this.imageRepository = imageRepository;
		this.trackRepository = trackRepository;
		this.userRepository = userRepository;
		this.tracklistRepository = tracklistRepository;
	}
	
	@GetMapping("/tracks")
	public String display (Model model, @AuthenticationPrincipal CurrentUser customUser) throws JsonProcessingException {
		
		model.addAttribute("albums", albumRepository.findByUsersContains(customUser.getUser()));
		return "track/tracks";
	}
	
	@GetMapping("/addForm/{id}")
	public String addForm (@PathVariable long id, @AuthenticationPrincipal CurrentUser customUser, Model model) {
		
		model.addAttribute("track", trackRepository.findById(id).get());
		model.addAttribute("tracklists", tracklistRepository.findAllByUserId(customUser.getUser().getId()));
		return "track/addForm";
		
	}
	
	@GetMapping("/addToTracklist/{id}")
	public String addToTracklist (@PathVariable long id, @RequestParam long tracklistId) {
		
		if (tracklistId != 0) {
			Tracklist tracklist = tracklistRepository.findById(tracklistId).get();
			tracklist.getTracks().add(trackRepository.findById(id).get());
			tracklistRepository.save(tracklist);
			return "redirect:/track/tracks";
		}
		return "redirect:/track/addForm/"+id+"";
	}
	
	@GetMapping("/addCreate/{id}")
	public String addCreate (@PathVariable long id, @RequestParam String name, @AuthenticationPrincipal CurrentUser customUser) {
		
		List<Track> songs = new ArrayList<>();
		songs.add(trackRepository.findById(id).get());
		
		Tracklist tracklist = Tracklist.builder()
				.date(Date.valueOf(LocalDate.now()))
				.name(name)
				.user(customUser.getUser())
				.tracks(songs)
				.build();
		tracklistRepository.save(tracklist);
		return "redirect:/track/tracks";
	}
	
	
}
