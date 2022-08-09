package com.example.marek.album;

import com.example.marek.image.Image;
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
@NoArgsConstructor
@AllArgsConstructor
public class Album {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long discogsId;
	private String artist;
	private String title;
	private String uri;
	private String label;
	private String catno;
	private String genre;
	private Date date;
	@OneToMany(cascade=CascadeType.REMOVE )
	private List<Track> tracks;
	@OneToMany(cascade=CascadeType.REMOVE )
	private List<Image> images;
	@ManyToMany(cascade=CascadeType.REMOVE )
	private List<User> users;

}
