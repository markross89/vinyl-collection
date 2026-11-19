
package com.example.marek.service.impl;

import com.example.marek.entity.Track;
import com.example.marek.entity.Tracklist;
import com.example.marek.entity.User;
import com.example.marek.entity.Album;
import com.example.marek.entity.PlaybackType;
import com.example.marek.dto.PlaybackTime;
import com.example.marek.repository.TrackRepository;
import com.example.marek.repository.TracklistRepository;
import com.example.marek.service.TracklistService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TracklistServiceImpl implements TracklistService {

    private final TracklistRepository tracklistRepository;
    private final TrackRepository trackRepository;

    public TracklistServiceImpl(
            TracklistRepository tracklistRepository,
            TrackRepository trackRepository) {

        this.tracklistRepository = tracklistRepository;
        this.trackRepository = trackRepository;
    }

    // --------------------------------------------------
    // TRACKLIST RETRIEVAL
    // --------------------------------------------------

    @Override
    public List<Tracklist> findAllByUserId(Long userId) {
        return tracklistRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<Tracklist> findByIdAndUserId(
            Long tracklistId,
            Long userId) {

        return tracklistRepository.findByIdAndUserId(
                tracklistId,
                userId
        );
    }

    // --------------------------------------------------
    // TRACKLIST CREATION
    // --------------------------------------------------

    @Override
    @Transactional
    public Optional<Tracklist> createTracklist(
            String name,
            User user) {

        if (name == null || name.trim().isEmpty() || user == null) {
            return Optional.empty();
        }

        String normalizedName = name.trim();

        if (tracklistRepository.existsByNameAndUserId(
                normalizedName,
                user.getId())) {
            return Optional.empty();
        }

        Tracklist tracklist = new Tracklist();
        tracklist.setName(normalizedName);
        tracklist.setDate(LocalDate.now());
        tracklist.setUser(user);

        return Optional.of(tracklistRepository.save(tracklist));
    }

    @Override
    @Transactional
    public Optional<Tracklist> createTracklistWithTrack(
            String name,
            Long trackId,
            User user) {

        if (name == null || name.trim().isEmpty()
                || trackId == null
                || user == null
                || user.getId() == null) {
            return Optional.empty();
        }

        String normalizedName = name.trim();

        if (tracklistRepository.existsByNameAndUserId(
                normalizedName,
                user.getId())) {
            return Optional.empty();
        }

        Optional<Track> optionalTrack =
                trackRepository.findByIdAndUserId(
                        trackId,
                        user.getId()
                );

        if (optionalTrack.isEmpty()) {
            return Optional.empty();
        }

        Tracklist tracklist = new Tracklist();
        tracklist.setName(normalizedName);
        tracklist.setDate(LocalDate.now());
        tracklist.setUser(user);
        tracklist.getTracks().add(optionalTrack.get());

        return Optional.of(tracklistRepository.save(tracklist));
    }

    // --------------------------------------------------
    // TRACK MANAGEMENT
    // --------------------------------------------------

    @Override
    @Transactional
    public boolean addTrackToTracklist(
            Long tracklistId,
            Long trackId,
            Long userId) {

        if (tracklistId == null || trackId == null || userId == null) {
            return false;
        }

        Optional<Tracklist> optionalTracklist =
                tracklistRepository.findByIdAndUserId(
                        tracklistId,
                        userId
                );

        if (optionalTracklist.isEmpty()) {
            return false;
        }

        Optional<Track> optionalTrack =
                trackRepository.findByIdAndUserId(
                        trackId,
                        userId
                );

        if (optionalTrack.isEmpty()) {
            return false;
        }

        Tracklist tracklist = optionalTracklist.get();
        Track track = optionalTrack.get();

        boolean alreadyContainsTrack = tracklist.getTracks()
                .stream()
                .anyMatch(existingTrack ->
                        existingTrack.getId().equals(trackId));

        if (alreadyContainsTrack) {
            return false;
        }

        tracklist.getTracks().add(track);

        return true;
    }

    // --------------------------------------------------
    // TRACKLIST DELETION
    // --------------------------------------------------

    @Override
    @Transactional
    public boolean deleteTracklist(Long tracklistId, Long userId) {

        Optional<Tracklist> tracklist =
                tracklistRepository.findByIdAndUserId(
                        tracklistId,
                        userId
                );

        if (tracklist.isEmpty()) {
            return false;
        }

        tracklistRepository.delete(tracklist.get());

        return true;
    }

    @Override
    @Transactional
    public boolean removeTrackFromTracklist(
            Long tracklistId,
            Long trackId,
            Long userId) {

        if (tracklistId == null || trackId == null || userId == null) {
            return false;
        }

        Optional<Tracklist> optionalTracklist =
                tracklistRepository.findByIdAndUserId(
                        tracklistId,
                        userId
                );

        if (optionalTracklist.isEmpty()) {
            return false;
        }

        Tracklist tracklist = optionalTracklist.get();

        boolean removed = tracklist.getTracks().removeIf(
                track -> trackId.equals(track.getId())
        );

        if (!removed) {
            return false;
        }

        tracklistRepository.save(tracklist);

        return true;
    }

    @Override
    @Transactional
    public boolean clearTracklist(Long tracklistId, Long userId) {

        if (tracklistId == null || userId == null) {
            return false;
        }

        Optional<Tracklist> optionalTracklist =
                tracklistRepository.findByIdAndUserId(
                        tracklistId,
                        userId
                );

        if (optionalTracklist.isEmpty()) {
            return false;
        }

        Tracklist tracklist = optionalTracklist.get();

        tracklist.getTracks().clear();

        tracklistRepository.save(tracklist);

        return true;
    }

    @Override
    @Transactional
    public boolean reorderTracks(
            Long tracklistId,
            Long userId,
            List<Long> trackIds) {

        if (tracklistId == null || userId == null || trackIds == null) {
            return false;
        }

        Optional<Tracklist> optionalTracklist =
                tracklistRepository.findByIdAndUserId(tracklistId, userId);

        if (optionalTracklist.isEmpty()) {
            return false;
        }

        Tracklist tracklist = optionalTracklist.get();

        List<Track> currentTracks = tracklist.getTracks();

        if (trackIds.size() != currentTracks.size()
                || !new HashSet<>(trackIds).equals(
                currentTracks.stream()
                        .map(Track::getId)
                        .collect(Collectors.toSet()))) {
            return false;
        }

        Map<Long, Track> tracksById = currentTracks.stream()
                .collect(Collectors.toMap(Track::getId, track -> track));

        List<Track> reorderedTracks = trackIds.stream()
                .map(tracksById::get)
                .collect(Collectors.toList());

        currentTracks.clear();
        currentTracks.addAll(reorderedTracks);

        tracklistRepository.save(tracklist);

        return true;
    }

    // --------------------------------------------------
    // TRACK RETRIEVAL
    // --------------------------------------------------

    @Override
    public List<Track> findTracksByTracklistIdAndUserId(
            Long tracklistId,
            Long userId) {

        Optional<Tracklist> tracklist =
                findByIdAndUserId(tracklistId, userId);

        if (tracklist.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(tracklist.get().getTracks());
    }

    @Override
    public List<Album> findAlbumsByTracklistIdAndUserId(
            Long tracklistId,
            Long userId) {

        Optional<Tracklist> optionalTracklist =
                findByIdAndUserId(tracklistId, userId);

        if (optionalTracklist.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, Album> albumsByDiscogsId = new LinkedHashMap<>();

        for (Track track : optionalTracklist.get().getTracks()) {

            Album album = track.getRelease();

            if (album != null && album.getDiscogsId() != null) {
                albumsByDiscogsId.putIfAbsent(
                        album.getDiscogsId(),
                        album
                );
            }
        }

        return new ArrayList<>(albumsByDiscogsId.values());
    }

    // --------------------------------------------------
    // TRACK SEARCH
    // --------------------------------------------------

    @Override
    public List<Track> searchTracksInTracklist(
            Long tracklistId,
            Long userId,
            String query) {

        Optional<Tracklist> tracklist =
                findByIdAndUserId(tracklistId, userId);

        if (tracklist.isEmpty()) {
            return new ArrayList<>();
        }

        List<Track> tracks =
                new ArrayList<>(tracklist.get().getTracks());

        if (query == null || query.trim().isEmpty()) {
            return tracks;
        }

        String searchQuery = query.trim().toLowerCase(Locale.ROOT);

        return tracks.stream()
                .filter(track ->
                        containsIgnoreCase(track.getTitle(), searchQuery)
                                || containsIgnoreCase(track.getArtist(), searchQuery)
                                || containsIgnoreCase(track.getAlbum(), searchQuery)
                                || containsIgnoreCase(track.getLabel(), searchQuery)
                )
                .collect(Collectors.toList());
    }

    // --------------------------------------------------
    // TRACK SORTING
    // --------------------------------------------------

    @Override
    public List<Track> sortTracksInTracklist(
            Long tracklistId,
            Long userId,
            String sort) {

        Optional<Tracklist> tracklist =
                findByIdAndUserId(tracklistId, userId);

        if (tracklist.isEmpty()) {
            return new ArrayList<>();
        }

        List<Track> tracks =
                new ArrayList<>(tracklist.get().getTracks());

        if ("manual".equalsIgnoreCase(sort)) {
            return tracks;
        }

        Comparator<Track> comparator;

        switch (sort == null ? "" : sort.toLowerCase(Locale.ROOT)) {

            case "artist":
                comparator = Comparator.comparing(
                        Track::getArtist,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                );
                break;

            case "album":
                comparator = Comparator.comparing(
                        Track::getAlbum,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                );
                break;

            case "label":
                comparator = Comparator.comparing(
                        Track::getLabel,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                );
                break;

            case "date":
                comparator = Comparator.comparing(
                        Track::getDate,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );
                break;

            case "duration":
                comparator = Comparator.comparingLong(
                        track -> parseDuration(track.getDuration())
                );
                break;

            case "title":
            default:
                comparator = Comparator.comparing(
                        Track::getTitle,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                );
                break;
        }

        tracks.sort(comparator);

        return tracks;
    }

    // --------------------------------------------------
    // PLAYBACK TIME
    // --------------------------------------------------

    @Override
    public PlaybackTime calculatePlaybackTime(
            Long tracklistId,
            Long userId) {

        Optional<Tracklist> optionalTracklist =
                findByIdAndUserId(tracklistId, userId);

        if (optionalTracklist.isEmpty()) {
            return null;
        }

        Tracklist tracklist = optionalTracklist.get();
        List<Track> tracks = tracklist.getTracks();

        if (tracks.isEmpty()) {
            return new PlaybackTime(0, false, 0);
        }

        long knownTotalSeconds = 0;
        int knownDurationCount = 0;
        int missingDurationCount = 0;

        for (Track track : tracks) {

            long durationSeconds = parseDuration(track.getDuration());

            if (durationSeconds == Long.MAX_VALUE || durationSeconds < 0) {
                missingDurationCount++;
            } else {
                knownTotalSeconds += durationSeconds;
                knownDurationCount++;
            }
        }

        long totalSeconds = knownTotalSeconds;

        // Estimate missing track durations
        // Estimate missing track durations
        if (missingDurationCount > 0) {

            long estimatedDuration;

            if (knownDurationCount > 0) {
                // Use average duration of tracks with known duration
                estimatedDuration = Math.round(
                        (double) knownTotalSeconds / knownDurationCount
                );
            } else {
                // No known durations: assume 5 minutes per track
                estimatedDuration = 300;
            }

            totalSeconds += estimatedDuration * missingDurationCount;
        }

        // DJ Mix: subtract transition time
        if (tracklist.getPlaybackType() == PlaybackType.DJ_MIX
                && tracks.size() > 1) {

            long transitionTime =
                    (long) (tracks.size() - 1)
                            * tracklist.getTransitionDuration();

            totalSeconds = Math.max(0, totalSeconds - transitionTime);
        }

        return new PlaybackTime(
                totalSeconds,
                missingDurationCount > 0,
                missingDurationCount
        );
    }

    @Override
    @Transactional
    public boolean updatePlaybackSettings(
            Long tracklistId,
            Long userId,
            PlaybackType playbackType,
            Integer transitionDuration) {

        if (tracklistId == null
                || userId == null
                || playbackType == null
                || transitionDuration == null
                || transitionDuration < 0
                || transitionDuration > 180) {
            return false;
        }

        Optional<Tracklist> optionalTracklist =
                tracklistRepository.findByIdAndUserId(tracklistId, userId);

        if (optionalTracklist.isEmpty()) {
            return false;
        }

        Tracklist tracklist = optionalTracklist.get();

        tracklist.setPlaybackType(playbackType);
        tracklist.setTransitionDuration(transitionDuration);

        tracklistRepository.save(tracklist);

        return true;
    }

    // --------------------------------------------------
    // PRIVATE HELPERS
    // --------------------------------------------------

    private boolean containsIgnoreCase(String value, String query) {
        return value != null
                && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private long parseDuration(String duration) {

        if (duration == null || duration.trim().isEmpty()) {
            return Long.MAX_VALUE;
        }

        try {
            String[] parts = duration.trim().split(":");

            long seconds = 0;

            for (String part : parts) {
                seconds = seconds * 60 + Long.parseLong(part);
            }

            return seconds;

        } catch (NumberFormatException exception) {
            return Long.MAX_VALUE;
        }
    }
}
