
package com.example.marek.service;

import com.example.marek.entity.Album;
import com.example.marek.entity.Box;
import com.example.marek.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BoxService {

    // =========================
    // BOX RETRIEVAL
    // =========================

    List<Box> findAllByUserId(Long userId);

    Optional<Box> findByIdAndUserId(Long boxId, Long userId);

    Optional<Album> findAlbumByDiscogsId(Long discogsId);

    boolean existsByNameAndUserId(String name, Long userId);


    // =========================
    // BOX CREATION
    // =========================

    Optional<Box> createEmptyBox(String name, User user);

    Optional<Box> createBoxWithAlbum(String name, User user, Album album);

    Optional<Box> createBoxWithAlbums(String name, User user, List<Album> albums);


    // =========================
    // BOX DELETION
    // =========================

    boolean deleteBox(Long boxId, Long userId);


    // =========================
    // ALBUM MANAGEMENT
    // =========================

    Optional<Boolean> addAlbumToBox(
            Long boxId,
            Long albumId,
            Long userId
    );

    Optional<Boolean> removeAlbumFromBox(
            Long boxId,
            Long albumId,
            Long userId
    );


    // =========================
    // AJAX DATA
    // =========================

    List<Map<String, Object>> getBoxesForAlbum(
            Album album,
            Long userId
    );
}
