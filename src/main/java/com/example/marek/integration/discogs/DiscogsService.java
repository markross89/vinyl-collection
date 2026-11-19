
package com.example.marek.integration.discogs;

import com.example.marek.entity.Album;
import com.example.marek.entity.Image;
import com.example.marek.entity.Track;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscogsService {

    private static final String PLACEHOLDER_IMAGE =
            "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg";

    private final DiscogsClient discogsClient;

    public DiscogsService(DiscogsClient discogsClient) {
        this.discogsClient = discogsClient;
    }

    public Map<String, String> getAlbumDetails(Map<String, Object> map) {
        Map<String, String> albumDetails = new HashMap<>();

        albumDetails.put("id", discogsClient.getAlbumId(map));
        albumDetails.put("artist", discogsClient.getAlbumArtist(map));
        albumDetails.put("title", discogsClient.getAlbumTitle(map));
        albumDetails.put("uri", discogsClient.getAlbumUri(map));
        albumDetails.put("genre", discogsClient.getAlbumGenre(map));
        albumDetails.put("label", discogsClient.getAlbumLabel(map));
        albumDetails.put("canto", discogsClient.getAlbumCatno(map));

        return albumDetails;
    }

    public List<Map<String, String>> getTracklist(Map<String, Object> map) {
        List<Map<String, String>> tracklist = new ArrayList<>();

        for (Object song : discogsClient.getAlbumTracklist(map)) {
            Map<String, String> track = new HashMap<>();

            track.put("position",
                    discogsClient.getTracklistSongDetail(song, "position"));
            track.put("title",
                    discogsClient.getTracklistSongDetail(song, "title"));
            track.put("duration",
                    discogsClient.getTracklistSongDetail(song, "duration"));

            tracklist.add(track);
        }

        return tracklist;
    }

    public List<Map<String, String>> getImages(Map<String, Object> map) {
        List<Map<String, String>> images = new ArrayList<>();
        List<?> pictures = discogsClient.getAlbumImages(map);

        if (pictures == null || pictures.isEmpty()) {
            Map<String, String> image = new HashMap<>();
            image.put("type", "primary");
            image.put("uri", PLACEHOLDER_IMAGE);
            images.add(image);

            return images;
        }

        for (Object picture : pictures) {
            Map<String, String> image = new HashMap<>();

            image.put("type",
                    discogsClient.getAlbumImageDetail(picture, "type"));
            image.put("uri",
                    discogsClient.getAlbumImageDetail(picture, "uri"));

            images.add(image);
        }

        return images;
    }


    public List<Map<String, String>> thumbsDisplay(Map<String, Object> map) {
        List<Map<String, String>> thumbs = new ArrayList<>();

        for (Object result : discogsClient.getThumbList(map)) {
            Map<String, String> album = new HashMap<>();

            String fullTitle = discogsClient.getThumbDetails(result, "title");
            String[] titleParts = fullTitle.split(" - ", 2);

            album.put("id", discogsClient.getThumbDetails(result, "id"));
            album.put("artist", titleParts.length > 1 ? titleParts[0] : "");
            album.put("title", titleParts.length > 1 ? titleParts[1] : fullTitle);
            album.put("image", discogsClient.getThumbDetails(result, "cover_image"));

            thumbs.add(album);
        }

        return thumbs;
    }


    public List<Map<String, String>> thumbsDisplayDatabase(List<Album> albums) {
        List<Map<String, String>> thumbs = new ArrayList<>();

        for (Album album : albums) {
            Map<String, String> map = new HashMap<>();

            map.put("id", String.valueOf(album.getDiscogsId()));
            map.put("title", String.join("-", album.getArtist(), album.getTitle()));

            String imageUri = album.getImages().stream()
                    .filter(image -> "primary".equals(image.getType()))
                    .map(Image::getUri)
                    .findFirst()
                    .orElse(PLACEHOLDER_IMAGE);

            map.put("image", imageUri);
            thumbs.add(map);
        }

        return thumbs;
    }

    public List<Track> saveAlbumTracks(Album album, Map<String, Object> map) {
        List<Track> tracks = new ArrayList<>();

        for (Object song : discogsClient.getAlbumTracklist(map)) {
            Track track = new Track();

            track.setPosition(
                    discogsClient.getTracklistSongDetail(song, "position"));
            track.setTitle(
                    discogsClient.getTracklistSongDetail(song, "title"));
            track.setDuration(
                    discogsClient.getTracklistSongDetail(song, "duration"));

            track.setAlbum(album.getTitle());
            track.setArtist(album.getArtist());
            track.setLabel(album.getLabel());
            track.setDiscogsId(album.getDiscogsId());
            track.setDate(LocalDate.now());
            track.setRelease(album);

            tracks.add(track);
        }

        return tracks;
    }

    public List<Image> saveAlbumImages(Album album, Map<String, Object> map) {
        List<Image> images = new ArrayList<>();
        List<?> pictures = discogsClient.getAlbumImages(map);

        if (pictures == null || pictures.isEmpty()) {
            Image image = new Image();

            image.setType("primary");
            image.setUri(PLACEHOLDER_IMAGE);
            image.setAlbum(album);

            images.add(image);

            return images;
        }

        for (Object picture : pictures) {
            Image image = new Image();

            image.setType(
                    discogsClient.getAlbumImageDetail(picture, "type"));
            image.setUri(
                    discogsClient.getAlbumImageDetail(picture, "uri"));
            image.setAlbum(album);

            images.add(image);
        }

        return images;
    }

    public Album createAlbum(Map<String, Object> map) {
        Long discogsId = Long.valueOf(discogsClient.getAlbumId(map));

        Album album = new Album();

        album.setDiscogsId(discogsId);
        album.setArtist(discogsClient.getAlbumArtist(map));
        album.setTitle(discogsClient.getAlbumTitle(map));
        album.setLabel(discogsClient.getAlbumLabel(map));
        album.setCatno(discogsClient.getAlbumCatno(map));
        album.setUri(discogsClient.getAlbumUri(map));
        album.setGenre(discogsClient.getAlbumGenre(map));
        album.setDate(LocalDate.now());

        List<Track> tracks = saveAlbumTracks(album, map);
        List<Image> images = saveAlbumImages(album, map);

        album.setTracks(tracks);
        album.setImages(images);

        return album;
    }
}
