package com.example.marek.album;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {


	Optional<Album> findAlbumByDiscogsId(long discogsId);


}
