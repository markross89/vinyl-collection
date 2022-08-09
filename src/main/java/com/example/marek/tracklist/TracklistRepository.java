package com.example.marek.tracklist;

import com.example.marek.box.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TracklistRepository  extends JpaRepository<Tracklist, Long> {

}
