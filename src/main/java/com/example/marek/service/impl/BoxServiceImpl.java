
package com.example.marek.service.impl;

import com.example.marek.entity.Album;
import com.example.marek.entity.Box;
import com.example.marek.entity.User;
import com.example.marek.repository.AlbumRepository;
import com.example.marek.repository.BoxRepository;
import com.example.marek.service.BoxService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoxServiceImpl implements BoxService {

    private final BoxRepository boxRepository;
    private final AlbumRepository albumRepository;

    public BoxServiceImpl(
            BoxRepository boxRepository,
            AlbumRepository albumRepository
    ) {
        this.boxRepository = boxRepository;
        this.albumRepository = albumRepository;
    }


    // =========================
    // BOX RETRIEVAL
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<Box> findAllByUserId(Long userId) {
        return boxRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Box> findByIdAndUserId(Long boxId, Long userId) {
        return boxRepository.findByIdAndUserId(boxId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Album> findAlbumByDiscogsId(Long discogsId) {
        return albumRepository.findByDiscogsId(discogsId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndUserId(String name, Long userId) {
        return boxRepository.existsByNameAndUserId(
                name.trim(),
                userId
        );
    }


    // =========================
    // BOX CREATION
    // =========================

    @Override
    public Optional<Box> createEmptyBox(String name, User user) {

        String boxName = name.trim();

        if (boxName.isEmpty()
                || boxRepository.existsByNameAndUserId(
                boxName,
                user.getId()
        )) {
            return Optional.empty();
        }

        Box box = new Box();
        box.setName(boxName);
        box.setDate(LocalDate.now());
        box.setUser(user);

        return Optional.of(boxRepository.save(box));
    }

    @Override
    public Optional<Box> createBoxWithAlbum(
            String name,
            User user,
            Album album
    ) {
        String boxName = name.trim();

        if (boxName.isEmpty()
                || boxRepository.existsByNameAndUserId(
                boxName,
                user.getId()
        )) {
            return Optional.empty();
        }

        Box box = new Box();
        box.setName(boxName);
        box.setDate(LocalDate.now());
        box.setUser(user);
        box.getAlbums().add(album);

        return Optional.of(boxRepository.save(box));
    }

    @Override
    public Optional<Box> createBoxWithAlbums(
            String name,
            User user,
            List<Album> albums) {

        if (name == null || name.trim().isEmpty() || user == null) {
            return Optional.empty();
        }

        String normalizedName = name.trim();

        if (boxRepository.existsByNameAndUserId(
                normalizedName,
                user.getId())) {
            return Optional.empty();
        }

        Box box = new Box();
        box.setName(normalizedName);
        box.setDate(LocalDate.now());
        box.setUser(user);

        if (albums != null) {
            box.getAlbums().addAll(albums);
        }

        return Optional.of(boxRepository.save(box));
    }


    // =========================
    // BOX DELETION
    // =========================

    @Override
    public boolean deleteBox(Long boxId, Long userId) {

        Optional<Box> optionalBox =
                boxRepository.findByIdAndUserId(boxId, userId);

        if (optionalBox.isEmpty()) {
            return false;
        }

        boxRepository.delete(optionalBox.get());

        return true;
    }


    // =========================
    // ALBUM MANAGEMENT
    // =========================

    @Override
    public Optional<Boolean> addAlbumToBox(
            Long boxId,
            Long albumId,
            Long userId
    ) {
        Optional<Box> optionalBox =
                boxRepository.findByIdAndUserId(boxId, userId);

        Optional<Album> optionalAlbum =
                albumRepository.findByDiscogsId(albumId);

        if (optionalBox.isEmpty() || optionalAlbum.isEmpty()) {
            return Optional.empty();
        }

        Box box = optionalBox.get();
        Album album = optionalAlbum.get();

        if (box.getAlbums().contains(album)) {
            return Optional.of(false);
        }

        box.getAlbums().add(album);

        return Optional.of(true);
    }

    @Override
    public Optional<Boolean> removeAlbumFromBox(
            Long boxId,
            Long albumId,
            Long userId
    ) {
        Optional<Box> optionalBox =
                boxRepository.findByIdAndUserId(boxId, userId);

        Optional<Album> optionalAlbum =
                albumRepository.findByDiscogsId(albumId);

        if (optionalBox.isEmpty() || optionalAlbum.isEmpty()) {
            return Optional.empty();
        }

        Box box = optionalBox.get();
        Album album = optionalAlbum.get();

        if (!box.getAlbums().contains(album)) {
            return Optional.of(false);
        }

        box.getAlbums().remove(album);

        return Optional.of(true);
    }


    // =========================
    // AJAX DATA
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBoxesForAlbum(
            Album album,
            Long userId
    ) {
        return boxRepository.findAllByUserId(userId)
                .stream()
                .map(box -> {
                    Map<String, Object> item = new LinkedHashMap<>();

                    item.put("id", box.getId());
                    item.put("name", box.getName());
                    item.put(
                            "containsAlbum",
                            box.getAlbums().contains(album)
                    );

                    return item;
                })
                .collect(Collectors.toList());
    }
}
