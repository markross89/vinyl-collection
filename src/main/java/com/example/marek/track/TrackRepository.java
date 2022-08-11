package com.example.marek.track;

import com.example.marek.album.Album;
import com.example.marek.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
	


}
