package com.example.marek.tracklist;

import com.example.marek.box.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TracklistRepository  extends JpaRepository<Tracklist, Long> {
	
	List<Tracklist> findAllByUserId(long userId);
	Tracklist findByName(String name);

}
