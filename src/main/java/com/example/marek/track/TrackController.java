package com.example.marek.track;

import com.example.marek.album.AlbumRepository;
import com.example.marek.tracklist.Tracklist;
import com.example.marek.tracklist.TracklistRepository;
import com.example.marek.user.CurrentUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional
@Controller
@RequestMapping("/track")
public class TrackController {
	
	private final AlbumRepository albumRepository;
	private final TrackRepository trackRepository;
	private final TracklistRepository tracklistRepository;
	
	public TrackController (AlbumRepository albumRepository, TrackRepository trackRepository,
							TracklistRepository tracklistRepository) {
		
		this.albumRepository = albumRepository;
		this.trackRepository = trackRepository;
		this.tracklistRepository = tracklistRepository;
	}
	
	@GetMapping("/tracks")  // display all songs
	public String display (Model model, @AuthenticationPrincipal CurrentUser customUser) throws JsonProcessingException {
		
		model.addAttribute("tracks", trackRepository.findTracksByUser(customUser.getUser().getId()));
		return "track/tracks";
	}
	
	@GetMapping("/addForm/{id}")  // display form with track list select / create new track list and add a song to it
	public String addForm (@PathVariable long id, @AuthenticationPrincipal CurrentUser customUser, Model model) {
		
		model.addAttribute("track", trackRepository.findById(id).get());
		model.addAttribute("tracklists", tracklistRepository.findAllByUserId(customUser.getUser().getId()));
		return "track/addForm";
		
	}
	
	@GetMapping("/addToTracklist/{id}")  // fulfill add form by selecting existing track list and add song to it
	public String addToTracklist (@PathVariable long id, @RequestParam long tracklistId) {
		
		if (tracklistId != 0) {
			if (trackRepository.findTracksFromTracklist(tracklistId).contains(trackRepository.findById(id).get())) {
				return "/track/messageTrack";
			}
			else {
				Tracklist tracklist = tracklistRepository.findById(tracklistId).get();
				tracklist.getTracks().add(trackRepository.findById(id).get());
				tracklistRepository.save(tracklist);
				return "redirect:/track/tracks";
			}
		}
		return "redirect:/track/addForm/"+id+"";
	}
	
	@GetMapping("/addCreate/{id}")  //  fulfill add form by creating new track list and add song to it
	public String addCreate (@PathVariable long id, @RequestParam String name, @AuthenticationPrincipal CurrentUser customUser) {
		
		if (tracklistRepository.findByNameAndUser(name, customUser.getUser()).isPresent()) {
			return "/track/messageTrack";
		}
		else {
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
	
	@GetMapping("/delete/{trackId}/{tracklistId}")  //  delete song from track list
	public String details (@PathVariable long trackId, @PathVariable long tracklistId) {
		
		trackRepository.deleteTrackTracklistConstrains(tracklistId, trackId);
		return "redirect:/tracklist/details/"+tracklistId+"";
	}
	
	

	@GetMapping("/searchCollection")  // search action for song collection
	public String searchCollection (@RequestParam String query, @AuthenticationPrincipal CurrentUser customUser, Model model) {
			model.addAttribute("tracks", trackRepository.searchSongCollection(customUser.getUser().getId(), query));
		return "track/tracks";
	}
	
	
	//   sorting actions for song collection
	
	@GetMapping("/sortBy/title")
	public String sortByTitle (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("tracks", trackRepository.sortAllUserSongsByTitle(customUser.getUser().getId()));
		return "/track/tracks";
	}
	
	@GetMapping("/sortBy/artist")
	public String sortByArtist (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("tracks", trackRepository.sortAllUserSongsByArtist(customUser.getUser().getId()));
		return "/track/tracks";
	}
	
	@GetMapping("/sortBy/album")
	public String sortByAlbum (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("tracks", trackRepository.sortAllUserSongsByAlbum(customUser.getUser().getId()));
		return "/track/tracks";
	}
	
	@GetMapping("/sortBy/label")
	public String sortByLabel (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("tracks", trackRepository.sortAllUserSongsByLabel(customUser.getUser().getId()));
		return "/track/tracks";
	}
	
	@GetMapping("/sortBy/date")
	public String sortByDate (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("tracks", trackRepository.sortAllUserSongsByDate(customUser.getUser().getId()));
		return "/track/tracks";
	}
	
	@GetMapping("/sortBy/duration")
	public String sortByDuration (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("tracks", trackRepository.sortAllUserSongsByDuration(customUser.getUser().getId()));
		return "/track/tracks";
	}
	
}
