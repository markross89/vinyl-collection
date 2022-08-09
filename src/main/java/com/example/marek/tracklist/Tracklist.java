package com.example.marek.tracklist;


import com.example.marek.track.Track;
import com.example.marek.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;


@Entity
@Table
@Getter
@Setter
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
	@ManyToOne(cascade=CascadeType.REMOVE )
	private User user;
	
}