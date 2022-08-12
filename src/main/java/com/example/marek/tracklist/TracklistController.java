package com.example.marek.tracklist;

import com.example.marek.user.CurrentUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;


@Controller
@RequestMapping("tracklist/")
public class TracklistController {
	
	private final TracklistRepository tracklistRepository;
	
	public TracklistController (TracklistRepository tracklistRepository) {
		
		this.tracklistRepository = tracklistRepository;
	}
	
	@GetMapping("/tracklists")
	public String display (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("tracklists", tracklistRepository.findAllByUserId(customUser.getUser().getId()));
		return "tracklist/tracklists";
	}
	
	@GetMapping("/addTracklist")
	public String add (@RequestParam String name, @AuthenticationPrincipal CurrentUser customUser) {
		
		Tracklist tracklist = Tracklist.builder()
				.date(Date.valueOf(LocalDate.now()))
				.name(name)
				.user(customUser.getUser())
				.build();
		tracklistRepository.save(tracklist);
		
		return "redirect:/tracklist/tracklists";
	}
	
	@GetMapping("/delete/{id}")
	public String delete (@PathVariable long id) {
		
		tracklistRepository.deleteById(id);
		return "redirect:/tracklist/tracklists";
	}
	
	@GetMapping("/details/{id}")
	public String details (@PathVariable long id, Model model) {
		
		model.addAttribute("name", tracklistRepository.findById(id).get());
	
		return "/tracklist/details";
	}
	@GetMapping("/addTrack/{id}")
	public String addTrack(@PathVariable long id, Model model) {
		
		model.addAttribute("name", tracklistRepository.findById(id).get());
		
		return "/tracklist/details";
	}
}
