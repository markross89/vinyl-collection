package com.example.marek.tracklist;


import com.example.marek.track.Track;
import com.example.marek.user.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;


@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tracklist {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private Date date;
	
	@ManyToMany
	private List<Track> tracks;
	
	@ManyToOne
	private User user;
	
}