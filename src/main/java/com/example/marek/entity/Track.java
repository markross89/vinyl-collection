package com.example.marek.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "track")
@Getter
@Setter
@NoArgsConstructor
public class Track {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long discogsId;

	private String title;
	private String position;
	private String duration;
	private String artist;
	private String album;
	private String label;

	private LocalDate date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id", nullable = false)
	private Album release;
}