package com.example.marek.tracklist;

import com.example.marek.album.Album;
import com.example.marek.album.AlbumRepository;
import com.example.marek.box.Box;
import com.example.marek.box.BoxRepository;
import com.example.marek.track.TrackRepository;
import com.example.marek.user.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/tracklist")
public class TracklistController {
	
	private final TracklistRepository tracklistRepository;
	private final TrackRepository trackRepository;
	private final AlbumRepository albumRepository;
	private final BoxRepository boxRepository;
	
	public TracklistController (TracklistRepository tracklistRepository, TrackRepository trackRepository, AlbumRepository albumRepository,
								BoxRepository boxRepository){
		
		this.tracklistRepository = tracklistRepository;
		this.trackRepository = trackRepository;
		this.albumRepository = albumRepository;
		this.boxRepository = boxRepository;
	}
	
	@GetMapping("/tracklists")  //display list of track lists
	public String display (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("tracklists", tracklistRepository.findAllByUserId(customUser.getUser().getId()));
		return "tracklist/tracklists";
	}
	
	@GetMapping("/addTracklist")  // add new track list
	public String add (@RequestParam String name, @AuthenticationPrincipal CurrentUser customUser) {
		
		if (tracklistRepository.findByNameAndUser(name, customUser.getUser()).isPresent()) {
			return "/tracklist/messageTracklist";
		}
		else {
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
		
		model.addAttribute("tracks", trackRepository.findTracksFromTracklist(id));
		model.addAttribute("name", tracklistRepository.findById(id).get());
		return "/tracklist/details";
	}
	
	@GetMapping("/createBoxForm/{tracklistId}") // display form to create box from track list used
	public String displayBoxForm (@PathVariable long tracklistId, Model model) {
		
		model.addAttribute("tracklistId", tracklistId);
		return "/box/addBoxFromTracklist";
	}
	
	@GetMapping("/createBoxFromTracklist/{tracklistId}")  // create a box and fills it with albums used for the track list
	public String createBox (@PathVariable long tracklistId, @RequestParam String name, @AuthenticationPrincipal CurrentUser customUser,
							 Model model) {
		
		List<Long> ids = albumRepository.findAlbumsIdByTracklist(tracklistId);
		List<Album> albums = new ArrayList<>();
		for (Long id : ids) {
			albums.add(albumRepository.findById(id).get());
		}
		if (boxRepository.findByName(name).isEmpty()) {
			Box box = Box.builder()
					.date(Date.valueOf(LocalDate.now()))
					.name(name)
					.user(customUser.getUser())
					.albums(albums)
					.build();
			boxRepository.save(box);
			return "redirect:/box/boxes";
		}
		model.addAttribute("id", tracklistId);
		return "/box/messageBox";
	}
	
	@GetMapping("/searchTracklist/{id}")  // search action for track list
	public String searchCollection (@PathVariable long id, @RequestParam String query, Model model) {
		model.addAttribute("tracks", trackRepository.searchTracklist(id, query));
		model.addAttribute("name", tracklistRepository.findById(id).get());
		return "tracklist/details";
	}
	
	
	//  sorting actions for track lists details view
	
	@GetMapping("/sortArtist/{id}")
	public String sortArtist (@PathVariable long id, Model model) {
		
		model.addAttribute("tracks", trackRepository.sortByArtist(id));
		model.addAttribute("name", tracklistRepository.findById(id).get());
		return "/tracklist/details";
	}
	
	@GetMapping("/sortTitle/{id}")
	public String sortTitle (@PathVariable long id, Model model) {
		
		model.addAttribute("tracks", trackRepository.sortByTitle(id));
		model.addAttribute("name", tracklistRepository.findById(id).get());
		return "/tracklist/details";
	}
	
	@GetMapping("/sortAlbum/{id}")
	public String sortAlbum (@PathVariable long id, Model model) {
		
		model.addAttribute("tracks", trackRepository.sortByAlbum(id));
		model.addAttribute("name", tracklistRepository.findById(id).get());
		return "/tracklist/details";
	}
	
	@GetMapping("/sortLabel/{id}")
	public String sortLabel (@PathVariable long id, Model model) {
		
		model.addAttribute("tracks", trackRepository.sortByLabel(id));
		model.addAttribute("name", tracklistRepository.findById(id).get());
		return "/tracklist/details";
	}
	
	@GetMapping("/sortDuration/{id}")
	public String sortDuration (@PathVariable long id, Model model) {
		
		model.addAttribute("tracks", trackRepository.sortByDuration(id));
		model.addAttribute("name", tracklistRepository.findById(id).get());
		return "/tracklist/details";
	}
	
	@GetMapping("/sortDate/{id}")
	public String sortDate (@PathVariable long id, Model model) {
		
		model.addAttribute("tracks", trackRepository.sortByDate(id));
		model.addAttribute("name", tracklistRepository.findById(id).get());
		return "/tracklist/details";
	}
	
}
