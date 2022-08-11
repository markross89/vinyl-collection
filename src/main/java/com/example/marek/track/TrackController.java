package com.example.marek.track;

import com.example.marek.album.Album;
import com.example.marek.album.AlbumRepository;
import com.example.marek.image.ImageRepository;
import com.example.marek.user.CurrentUser;
import com.example.marek.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/track")
public class TrackController {
	
	private final AlbumRepository albumRepository;
	private final ImageRepository imageRepository;
	private final TrackRepository trackRepository;
	private final UserRepository userRepository;
	
	public TrackController (AlbumRepository albumRepository, ImageRepository imageRepository, TrackRepository trackRepository,
							UserRepository userRepository) {
		
		this.albumRepository = albumRepository;
		this.imageRepository = imageRepository;
		this.trackRepository = trackRepository;
		this.userRepository = userRepository;
	}
	
	@GetMapping("/tracks")
	public String display (Model model, @AuthenticationPrincipal CurrentUser customUser) throws JsonProcessingException {
		
		model.addAttribute("albums", albumRepository.findByUsersContains(customUser.getUser()));

		
		return "track/tracks";
	}
}
