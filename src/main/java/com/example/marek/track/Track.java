package com.example.marek.track;

import com.example.marek.album.Album;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Track {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	String title;
	String position;
	String duration;

	
}
