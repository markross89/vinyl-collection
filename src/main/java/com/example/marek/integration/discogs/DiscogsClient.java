
package com.example.marek.integration.discogs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class DiscogsClient {

    private static final String USER_AGENT =
            "VinylCollection/1.0";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DiscogsClient(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        this.objectMapper = objectMapper;
    }

    public Map<String, Object> mapRequestData(String requestAddress)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestAddress))
                .timeout(Duration.ofSeconds(20))
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/vnd.discogs.v2.discogs+json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException(
                    "Discogs API returned HTTP status: "
                            + response.statusCode()
                            + ". Response: "
                            + response.body()
            );
        }

        return objectMapper.readValue(
                response.body(),
                new TypeReference<Map<String, Object>>() {}
        );
    }

    public String getAlbumId(Map<String, Object> map) {
        return map.get("id").toString();
    }

    public String getAlbumArtist(Map<String, Object> map) {
        return ((Map<?, ?>) ((List<?>) map.get("artists")).get(0))
                .get("name").toString();
    }

    public String getAlbumTitle(Map<String, Object> map) {
        return map.get("title").toString();
    }

    public String getAlbumUri(Map<String, Object> map) {
        return map.get("uri").toString();
    }

    public String getAlbumLabel(Map<String, Object> map) {
        return ((Map<?, ?>) ((List<?>) map.get("labels")).get(0))
                .get("name").toString();
    }

    public String getAlbumCatno(Map<String, Object> map) {
        return ((Map<?, ?>) ((List<?>) map.get("labels")).get(0))
                .get("catno").toString();
    }

    public String getAlbumGenre(Map<String, Object> map) {
        return ((List<?>) map.get("genres")).get(0).toString();
    }

    public List<?> getAlbumTracklist(Map<String, Object> map) {
        return (List<?>) map.get("tracklist");
    }

    public String getTracklistSongDetail(Object song, String value) {
        return ((Map<?, ?>) song).get(value).toString();
    }

    public List<?> getAlbumImages(Map<String, Object> map) {
        return (List<?>) map.get("images");
    }

    public String getAlbumImageDetail(Object image, String value) {
        return ((Map<?, ?>) image).get(value).toString();
    }

    public List<?> getThumbList(Map<String, Object> map) {
        return (List<?>) map.get("results");
    }

    public String getThumbDetails(Object thumb, String value) {
        return ((Map<?, ?>) thumb).get(value).toString();
    }
}
