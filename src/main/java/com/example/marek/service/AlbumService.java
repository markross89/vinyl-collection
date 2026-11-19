
package com.example.marek.service;

import com.example.marek.entity.Album;
import com.example.marek.entity.User;

import java.util.List;

public interface AlbumService {

    List<Album> findAlbumsByUser(User user);

    Album findByDiscogsId(Long discogsId);

    void removeAlbumFromUser(Long discogsId, User user);

    boolean addAlbumToUser(Long discogsId, User user);

    boolean isAlbumInCollection(Long discogsId, Long userId);
}
