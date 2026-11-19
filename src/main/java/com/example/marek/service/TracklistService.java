
package com.example.marek.service;

import com.example.marek.entity.Track;
import com.example.marek.entity.Tracklist;
import com.example.marek.entity.User;
import com.example.marek.entity.Album;
import com.example.marek.dto.PlaybackTime;
import com.example.marek.entity.PlaybackType;

import java.util.List;
import java.util.Optional;

public interface TracklistService {

    // TRACKLIST RETRIEVAL

    List<Tracklist> findAllByUserId(Long userId);

    Optional<Tracklist> findByIdAndUserId(
            Long tracklistId,
            Long userId
    );

    // TRACKLIST CREATION

    Optional<Tracklist> createTracklist(
            String name,
            User user
    );

    // TRACKLIST DELETION

    boolean deleteTracklist(
            Long tracklistId,
            Long userId
    );

    boolean removeTrackFromTracklist(
            Long tracklistId,
            Long trackId,
            Long userId
    );

    boolean addTrackToTracklist(
            Long tracklistId,
            Long trackId,
            Long userId
    );

    Optional<Tracklist> createTracklistWithTrack(
            String name,
            Long trackId,
            User user
    );
    // TRACK RETRIEVAL

    List<Track> findTracksByTracklistIdAndUserId(
            Long tracklistId,
            Long userId
    );

    List<Album> findAlbumsByTracklistIdAndUserId(
            Long tracklistId,
            Long userId
    );

    boolean clearTracklist(
            Long tracklistId,
            Long userId
    );

    boolean reorderTracks(
            Long tracklistId,
            Long userId,
            List<Long> trackIds
    );

    // TRACK SEARCH

    List<Track> searchTracksInTracklist(
            Long tracklistId,
            Long userId,
            String query
    );

    // TRACK SORTING

    List<Track> sortTracksInTracklist(
            Long tracklistId,
            Long userId,
            String sort
    );

    // PLAYBACK TIME

    PlaybackTime calculatePlaybackTime(
            Long tracklistId,
            Long userId
    );

    boolean updatePlaybackSettings(
            Long tracklistId,
            Long userId,
            PlaybackType playbackType,
            Integer transitionDuration
    );
}
