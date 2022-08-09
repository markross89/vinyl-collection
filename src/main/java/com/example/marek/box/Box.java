package com.example.marek.box;

import com.example.marek.album.Album;
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
public class Box {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private Date date;
	@ManyToMany
	private List<Album> albums;
	@ManyToOne(cascade=CascadeType.REMOVE )
	private User user;

}
