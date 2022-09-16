package com.example.marek.homeHttp;

import com.example.marek.album.Album;
import com.example.marek.album.AlbumRepository;
import com.example.marek.image.Image;
import com.example.marek.image.ImageRepository;
import com.example.marek.track.Track;
import com.example.marek.track.TrackRepository;
import com.example.marek.user.User;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Transactional
public class ApiService {
	
	
	private final ApiController apiController;
	private final TrackRepository trackRepository;
	private final AlbumRepository albumRepository;
	private final ImageRepository imageRepository;
	
	public ApiService (ApiController apiController, TrackRepository trackRepository, AlbumRepository albumRepository,
					   ImageRepository imageRepository) {
		
		this.apiController = apiController;
		this.trackRepository = trackRepository;
		this.albumRepository = albumRepository;
		this.imageRepository = imageRepository;
	}
	
	
	public Map<String, String> getAlbumDetails (Map<String, Object> map) {
		
		Map<String, String> albumDetails = new HashMap<>();
		albumDetails.put("id", apiController.getAlbumId(map));
		albumDetails.put("artist", apiController.getAlbumArtist(map));
		albumDetails.put("title", apiController.getAlbumTitle(map));
		albumDetails.put("uri", apiController.getAlbumUri(map));
		albumDetails.put("genre", apiController.getAlbumGenre(map));
		albumDetails.put("label", apiController.getAlbumLabel(map));
		albumDetails.put("canto", apiController.getAlbumCatno(map));
		return albumDetails;
	}
	
	public List<Map<String, String>> getTracklist (Map<String, Object> map) {
		
		List<Map<String, String>> tracklist = new ArrayList<>();
		List songs = apiController.getAlbumTracklist(map);
		for (Object song : songs) {
			Map<String, String> track = new HashMap<>();
			track.put("position", apiController.getTracklistSongDetail(song, "position"));
			track.put("title", apiController.getTracklistSongDetail(song, "title"));
			track.put("duration", apiController.getTracklistSongDetail(song, "duration"));
			tracklist.add(track);
		}
		return tracklist;
	}
	
	public List<Map<String, String>> getImages (Map<String, Object> map) {
		
		List<Map<String, String>> images = new ArrayList<>();
		List pictures = apiController.getAlbumImages(map);
		if (pictures == null) {
			Map<String, String> mapa = new HashMap<>();
			mapa.put("type", "primary");
			mapa.put("uri", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/330px-No-Image-Placeholder.svg.png");
			images.add(mapa);
		}
		else {
			for (Object picture : pictures) {
				Map<String, String> image = new HashMap<>();
				image.put("type", apiController.getAlbumImageDetail(picture, "type"));
				image.put("uri", apiController.getAlbumImageDetail(picture, "uri"));
				images.add(image);
			}
		}
		return images;
	}
	
	public List<Map<String, String>> thumbsDisplay (Map map) {
		
		List<Map<String, String>> thumbs = new ArrayList<>();
		for (Object o : apiController.getThumbList(map)) {
			Map<String, String> album = new HashMap<>();
			album.put("id", apiController.getThumbDetails(o, "id"));
			album.put("title", apiController.getThumbDetails(o, "title"));
			album.put("image", apiController.getThumbDetails(o, "cover_image"));
			thumbs.add(album);
		}
		
		return thumbs;
	}
	
	public List<Map<String, String>> thumbsDisplayDatabase (List<Album> albums) {
		
		List<Map<String, String>> thumbs = new ArrayList<>();
		for (Album a : albums) {
			Map<String, String> map = new HashMap<>();
			map.put("id", String.valueOf(a.getDiscogsId()));
			map.put("title", String.join("-", a.getArtist(), a.getTitle()));
			map.put("image", a.getImages().get(0).getUri());
			thumbs.add(map);
		}
		return thumbs;
	}
	
	public List<Track> saveAlbumTracks (long id, Map<String, Object> map) {
		
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
		return tracks;
	}
	
	public List<Image> saveAlbumImages (Map<String, Object> map) {
		
		List<Image> images = new ArrayList<>();
		List list = apiController.getAlbumImages(map);
		
		if (list == null) {
			images.add(imageRepository.findById(60l).get());
		}
		else {
			for (Object o : list) {
				Image image = Image.builder().type(apiController.getAlbumImageDetail(o, "type"))
						.uri(apiController.getAlbumImageDetail(o, "uri")).build();
				images.add(image);
				imageRepository.save(image);
			}
		}
		return images;
		
	}
	
	public void saveAlbum (List<Image> images, List<Track> tracks, Map<String, Object> map, User user, long id) {
		
		LocalDate date = LocalDate.now();
		List<User> usersList = new ArrayList<>();
		usersList.add(user);
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
	
}
