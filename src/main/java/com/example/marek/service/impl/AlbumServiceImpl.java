
package com.example.marek.service.impl;

import com.example.marek.entity.Album;
import com.example.marek.entity.Box;
import com.example.marek.entity.Tracklist;
import com.example.marek.entity.User;
import com.example.marek.integration.discogs.DiscogsClient;
import com.example.marek.integration.discogs.DiscogsService;
import com.example.marek.repository.AlbumRepository;
import com.example.marek.repository.BoxRepository;
import com.example.marek.repository.TracklistRepository;
import com.example.marek.service.AlbumService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {

    private static final String DISCOGS_RELEASE_URL =
            "https://api.discogs.com/releases/";

    private final AlbumRepository albumRepository;
    private final BoxRepository boxRepository;
    private final TracklistRepository tracklistRepository;
    private final DiscogsClient discogsClient;
    private final DiscogsService discogsService;

    public AlbumServiceImpl(
            AlbumRepository albumRepository,
            BoxRepository boxRepository,
            TracklistRepository tracklistRepository,
            DiscogsClient discogsClient,
            DiscogsService discogsService) {

        this.albumRepository = albumRepository;
        this.boxRepository = boxRepository;
        this.tracklistRepository = tracklistRepository;
        this.discogsClient = discogsClient;
        this.discogsService = discogsService;
    }

    // --------------------------------------------------
    // FIND USER ALBUMS
    // --------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<Album> findAlbumsByUser(User user) {
        return albumRepository.findDistinctByUsersContains(user);
    }

    // --------------------------------------------------
    // FIND ALBUM BY DISCOGS ID
    // --------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public Album findByDiscogsId(Long discogsId) {

        return albumRepository.findByDiscogsId(discogsId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Album not found. Discogs ID: " + discogsId
                ));
    }

    // --------------------------------------------------
    // ADD ALBUM TO USER COLLECTION
    // --------------------------------------------------

    @Override
    public boolean addAlbumToUser(Long discogsId, User user) {

        if (discogsId == null || user == null || user.getId() == null) {
            throw new IllegalArgumentException(
                    "Discogs ID and user are required."
            );
        }

        // Check whether the album already belongs to this user.
        if (albumRepository.existsByDiscogsIdAndUsers_Id(
                discogsId,
                user.getId())) {

            return false;
        }

        // Find the album in the database.
        Album album = albumRepository.findByDiscogsId(discogsId)
                .orElse(null);

        // Download and create the album if it does not exist.
        if (album == null) {

            try {

                Map<String, Object> albumData =
                        discogsClient.mapRequestData(
                                DISCOGS_RELEASE_URL + discogsId
                        );

                album = discogsService.createAlbum(albumData);

            } catch (Exception exception) {

                throw new IllegalStateException(
                        "Could not download album from Discogs. ID: "
                                + discogsId,
                        exception
                );
            }

            album.setUsers(new HashSet<>());
        }

        // Assign the album to the user.
        album.getUsers().add(user);

        // Save the album and its relationships.
        albumRepository.save(album);

        return true;
    }

    // --------------------------------------------------
    // REMOVE ALBUM FROM USER COLLECTION
    // --------------------------------------------------

    @Override
    public void removeAlbumFromUser(Long discogsId, User user) {

        Album album = findByDiscogsId(discogsId);

        // Check whether the album belongs to the user.
        boolean belongsToUser = album.getUsers().stream()
                .anyMatch(albumUser ->
                        albumUser.getId().equals(user.getId())
                );

        if (!belongsToUser) {
            throw new IllegalArgumentException(
                    "Album does not belong to this user."
            );
        }

        // Remove album tracks from user's tracklists.
        List<Tracklist> tracklists =
                tracklistRepository.findAllByUserId(user.getId());

        for (Tracklist tracklist : tracklists) {

            tracklist.getTracks().removeIf(track ->
                    track.getRelease().getId().equals(album.getId())
            );
        }

        // Remove album from user's boxes.
        List<Box> boxes = boxRepository.findAllByUserId(user.getId());

        for (Box box : boxes) {
            box.getAlbums().remove(album);
        }

        // Remove the relationship between album and user.
        album.getUsers().removeIf(albumUser ->
                albumUser.getId().equals(user.getId())
        );

        // Delete the album if it no longer belongs to any user.
        if (album.getUsers().isEmpty()) {
            albumRepository.delete(album);
        }
    }

    @Override
    public boolean isAlbumInCollection(Long discogsId, Long userId) {
        if (discogsId == null || userId == null) {
            return false;
        }

        return albumRepository.existsByDiscogsIdAndUsers_Id(discogsId, userId);
    }
}
