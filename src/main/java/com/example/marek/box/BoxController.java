package com.example.marek.box;

import com.example.marek.album.Album;
import com.example.marek.album.AlbumRepository;
import com.example.marek.homeHttp.ApiController;
import com.example.marek.track.Track;
import com.example.marek.tracklist.Tracklist;
import com.example.marek.user.CurrentUser;
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
import java.util.List;


@Transactional
@Controller
@RequestMapping("/box")
public class BoxController {
	
	private final ApiController apiController;
	private final BoxRepository boxRepository;
	private final AlbumRepository albumRepository;
	
	public BoxController (ApiController apiController, BoxRepository boxRepository, AlbumRepository albumRepository) {
		
		this.apiController = apiController;
		this.boxRepository = boxRepository;
		this.albumRepository = albumRepository;
	}
	
	@GetMapping("/boxes")  //display list of boxes
	public String display (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("boxes", boxRepository.findAllByUserId(customUser.getUser().getId()));
		return "box/boxes";
	}
	
	@GetMapping("/addBox")  // add new box
	public String add (@RequestParam String name, @AuthenticationPrincipal CurrentUser customUser) {
		
		if (boxRepository.findByName(name).isPresent()) {
			return "/box/messageBox";
		}
		else {
			Box box = Box.builder()
					.date(Date.valueOf(LocalDate.now()))
					.name(name)
					.user(customUser.getUser())
					.build();
			boxRepository.save(box);
			return "redirect:/box/boxes";
		}
	}
	
	@GetMapping("/delete/{id}")  // delete box
	public String delete (@PathVariable long id) {
		
		boxRepository.deleteById(id);
		return "redirect:/box/boxes";
	}
	
	@GetMapping("/details/{id}")  //  display details of box
	public String details (@PathVariable long id, Model model) {
		
		List<Album> albums = boxRepository.findById(id).get().getAlbums();
		model.addAttribute("thumbs", apiController.thumbsDisplayDatabase(albums));
		model.addAttribute("box", boxRepository.findById(id).get());
		return "/box/details";
	}
	
	@GetMapping("/removeAlbum/{discogsId}/{boxId}")  //  removes album from box
	public String remove (@PathVariable long discogsId, @PathVariable long boxId) {
		
		boxRepository.deleteBoxAlbumConstrainsWithBoxId(boxId, albumRepository.findAlbumByDiscogsId(discogsId).getId());
		return "redirect:/box/details/"+boxId+"";
	}
	
	@GetMapping("/addForm/{id}")  // display form with box select / create new box and add a song to it
	public String addForm (@PathVariable long id, @AuthenticationPrincipal CurrentUser customUser, Model model) {
		
		model.addAttribute("album", albumRepository.findAlbumByDiscogsId(id));
		model.addAttribute("boxes", boxRepository.findAllByUserId(customUser.getUser().getId()));
		return "box/addForm";
		
	}
	
	@GetMapping("/addAlbum/{discogsId}")  //  adds album to box
	public String addAlbum (@PathVariable long discogsId, @RequestParam long boxId) {
		
		
		if (boxId != 0) {
			Album album = albumRepository.findAlbumByDiscogsId(discogsId);
			Box box = boxRepository.findById(boxId).get();
			
			if (box.getAlbums().contains(album)) {
				return "/box/messageAlbum";
			}
			else {
				List<Album> albums = box.getAlbums();
				albums.add(album);
				box.setAlbums(albums);
				boxRepository.save(box);
			}
			return "redirect:/box/details/"+boxId+"";
		}
		return "redirect:/box/addForm/"+discogsId+"";
	}
	
	@GetMapping("/addCreate/{discogsId}")  //  fulfill add form by creating new track list and add song to it
	public String addCreate (@PathVariable long discogsId, @RequestParam String name, @AuthenticationPrincipal CurrentUser customUser) {
		
		
		if (boxRepository.findByName(name).isPresent()) {
			return "/box/messageAlbum";
		}
		else {
			List<Album> albums = new ArrayList<>();
			albums.add(albumRepository.findAlbumByDiscogsId(discogsId));
			Box box = Box.builder()
					.date(Date.valueOf(LocalDate.now()))
					.name(name)
					.user(customUser.getUser())
					.albums(albums)
					.build();
			boxRepository.save(box);
			return "redirect:/box/boxes";
		}
	}
	
}
