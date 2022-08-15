package com.example.marek.tracklist;

import com.example.marek.track.Track;
import com.example.marek.track.TrackRepository;
import com.example.marek.user.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/tracklist")
public class TracklistController {
	
	private final TracklistRepository tracklistRepository;
	private final TrackRepository trackRepository;
	
	public TracklistController (TracklistRepository tracklistRepository, TrackRepository trackRepository) {
		
		this.tracklistRepository = tracklistRepository;
		this.trackRepository = trackRepository;
	}
	
	@GetMapping("/tracklists")  //display list of track lists
	public String display (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("tracklists", tracklistRepository.findAllByUserId(customUser.getUser().getId()));
		return "tracklist/tracklists";
	}
	
	@GetMapping("/addTracklist")  // add new track list
	public String add (@RequestParam String name, @AuthenticationPrincipal CurrentUser customUser) {
		if(tracklistRepository.findByName(name).isPresent()){
			return "/tracklist/messageTracklist";
		}else {
			Tracklist tracklist = Tracklist.builder()
					.date(Date.valueOf(LocalDate.now()))
					.name(name)
					.user(customUser.getUser())
					.build();
			tracklistRepository.save(tracklist);
			
			return "redirect:/tracklist/tracklists";
		}
	}
	
	@GetMapping("/delete/{id}")  // delete track list
	public String delete (@PathVariable long id) {
		
		tracklistRepository.deleteById(id);
		return "redirect:/tracklist/tracklists";
	}
	
	@GetMapping("/details/{id}")  //  display details of track list
	public String details (@PathVariable long id, Model model) {
		
	model.addAttribute("tracks",trackRepository.findTracksFromTracklist(id));
	model.addAttribute("name", tracklistRepository.findById(id).get());
	
		return "/tracklist/details";
	}
	
}
