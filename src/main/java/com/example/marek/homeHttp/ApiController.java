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
	
}
