package com.example.marek.homeHttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Transactional
public class ApiController {
	
	

	
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
		for (Object picture : pictures) {
			Map<String, String> image = new HashMap<>();
			image.put("type", getAlbumImageDetail(picture, "type"));
			image.put("uri", getAlbumImageDetail(picture, "uri"));
			images.add(image);
		}
		return images;
	}
	
	
	public List<Map<String, String>> thumbsDisplay(Map map){
		
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

}
