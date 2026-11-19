package com.example.marek.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.example.marek.entity.PlaybackType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tracklist")
@Getter
@Setter
@NoArgsConstructor
public class Tracklist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private LocalDate date;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PlaybackType playbackType = PlaybackType.NORMAL;

	@Column(nullable = false)
	private Integer transitionDuration = 60;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToMany
	@JoinTable(
			name = "tracklist_tracks",
			joinColumns = @JoinColumn(name = "tracklist_id"),
			inverseJoinColumns = @JoinColumn(name = "tracks_id")
	)
	@OrderColumn(name = "track_order")
	private List<Track> tracks = new ArrayList<>();
}