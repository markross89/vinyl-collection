package com.example.marek.homeHttp;

import com.example.marek.album.Album;
import com.example.marek.album.AlbumRepository;
import com.example.marek.image.Image;
import com.example.marek.image.ImageRepository;
import com.example.marek.track.Track;
import com.example.marek.track.TrackRepository;
import com.example.marek.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Transactional
public class ApiController {
	
	private final TrackRepository trackRepository;
	private final ImageRepository imageRepository;
	private final AlbumRepository albumRepository;
	
	public ApiController (TrackRepository trackRepository, ImageRepository imageRepository, AlbumRepository albumRepository) {
		
		this.trackRepository = trackRepository;
		this.imageRepository = imageRepository;
		this.albumRepository = albumRepository;
	}
	
	
	public Map<String, Object> mapRequestData (String requestAddress) throws JsonProcessingException {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(requestAddress))
				.build();
		
		HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
		String json = response.body();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> AlbumMap = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
		});
		
		return AlbumMap;
	}
	
	public String getAlbumId (Map<String, Object> map) {
		
		return map.get("id").toString();
	}
	
	
	public String getAlbumArtist (Map<String, Object> map) {
		
		return ((Map) (((List) map.get("artists")).get(0))).get("name").toString();
	}
	
	public String getAlbumTitle (Map<String, Object> map) {
		
		return map.get("title").toString();
	}
	
	public String getAlbumUri (Map<String, Object> map) {
		
		return map.get("uri").toString();
	}
	
	public String getAlbumLabel (Map<String, Object> map) {
		
		return ((Map) (((List) map.get("labels")).get(0))).get("name").toString();
	}
	
	public String getAlbumCatno (Map<String, Object> map) {
		
		return ((Map) ((List) map.get("labels")).get(0)).get("catno").toString();
	}
	
	public String getAlbumGenre (Map<String, Object> map) {
		
		return (((List) map.get("genres")).get(0)).toString();
	}
	
	public List getAlbumTracklist (Map<String, Object> map) {
		
		return (List) map.get("tracklist");
	}
	
	public String getTracklistSongDetail (Object song, String value) {
		
		return ((Map) song).get(value).toString();
	}
	
	public List getAlbumImages (Map<String, Object> map) {
		
		return (List) map.get("images");
	}
	
	public String getAlbumImageDetail (Object image, String value) {
		
		return ((Map) image).get(value).toString();
	}
	
	public List getThumbList (Map<String, Object> map) {
		
		return ((List) map.get("results"));
	}
	
	public String getThumbDetails (Object thumb, String value) {
		
		return ((Map) thumb).get(value).toString();
	}
	
	public Map<String, String> getAlbumDetails (Map<String, Object> map) {
		
		Map<String, String> albumDetails = new HashMap<>();
		albumDetails.put("id", getAlbumId(map));
		albumDetails.put("artist", getAlbumArtist(map));
		albumDetails.put("title", getAlbumTitle(map));
		albumDetails.put("uri", getAlbumUri(map));
		albumDetails.put("genre", getAlbumGenre(map));
		albumDetails.put("label", getAlbumLabel(map));
		albumDetails.put("canto", getAlbumCatno(map));
		return albumDetails;
	}
	
	public List<Map<String, String>> getTracklist (Map<String, Object> map) {
		
		List<Map<String, String>> tracklist = new ArrayList<>();
		List songs = getAlbumTracklist(map);
		for (Object song : songs) {
			Map<String, String> track = new HashMap<>();
			track.put("position", getTracklistSongDetail(song, "position"));
			track.put("title", getTracklistSongDetail(song, "title"));
			track.put("duration", getTracklistSongDetail(song, "duration"));
			tracklist.add(track);
		}
		return tracklist;
	}
	
	public List<Map<String, String>> getImages (Map<String, Object> map) {
		
		List<Map<String, String>> images = new ArrayList<>();
		List pictures = getAlbumImages(map);
		if (pictures == null) {
			Map<String, String> mapa = new HashMap<>();
			mapa.put("type", "primary");
			mapa.put("uri", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/330px-No-Image-Placeholder.svg.png");
			images.add(mapa);
		}
		else {
			for (Object picture : pictures) {
				Map<String, String> image = new HashMap<>();
				image.put("type", getAlbumImageDetail(picture, "type"));
				image.put("uri", getAlbumImageDetail(picture, "uri"));
				images.add(image);
			}
		}
		return images;
	}
	
	
	public List<Map<String, String>> thumbsDisplay (Map map) {
		
		List<Map<String, String>> thumbs = new ArrayList<>();
		for (Object o : getThumbList(map)) {
			Map<String, String> album = new HashMap<>();
			album.put("id", getThumbDetails(o, "id"));
			album.put("title", getThumbDetails(o, "title"));
			album.put("image", getThumbDetails(o, "cover_image"));
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
		for (Object o : getAlbumTracklist(map)) {
			Track track = Track.builder().position(getTracklistSongDetail(o, "position"))
					.title(getTracklistSongDetail(o, "title"))
					.duration(getTracklistSongDetail(o, "duration"))
					.album(getAlbumTitle(map))
					.artist(getAlbumArtist(map))
					.label(getAlbumLabel(map))
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
		List list = getAlbumImages(map);
		
		if (list == null) {
			images.add(imageRepository.findById(60l).get());
		}
		else {
			for (Object o : list) {
				Image image = Image.builder().type(getAlbumImageDetail(o, "type"))
						.uri(getAlbumImageDetail(o, "uri")).build();
				images.add(image);
				imageRepository.save(image);
			}
		}
		return images;
		
	}
	public void saveAlbum (List<Image> images, List<Track> tracks, Map<String, Object> map, User user,long id) {
		
		LocalDate date = LocalDate.now();
		List<User> usersList = new ArrayList<>();
		usersList.add(user);
		Album album = Album.builder().discogsId(id)
				.artist(getAlbumArtist(map))
				.title(getAlbumTitle(map))
				.label(getAlbumLabel(map))
				.catno(getAlbumCatno(map))
				.uri(getAlbumUri(map))
				.genre(getAlbumGenre(map))
				.images(images)
				.tracks(tracks)
				.date(Date.valueOf(date))
				.users(usersList).build();
		albumRepository.save(album);
	}
}
