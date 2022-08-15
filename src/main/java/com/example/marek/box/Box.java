package com.example.marek.box;

import com.example.marek.album.Album;
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
public class Box {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private Date date;
	@ManyToMany
	private List<Album> albums;
	@ManyToOne
	private User user;

}
