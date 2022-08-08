package com.example.marek.track;

import com.example.marek.album.Album;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Track {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	String Title;
	String position;
	String duration;

	
}
