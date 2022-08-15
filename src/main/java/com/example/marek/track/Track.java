package com.example.marek.track;


import lombok.*;

import javax.persistence.*;
import java.sql.Date;



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
	long discogsId;
	String title;
	String position;
	String duration;
	String artist;
	String album;
	String label;
	Date date;

	
}
