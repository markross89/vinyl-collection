package com.example.marek.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "album")
@Getter
@Setter
@NoArgsConstructor
public class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private Long discogsId;

	private String artist;
	private String title;
	private String uri;
	private String label;
	private String catno;
	private String genre;

	private LocalDate date;

	@OneToMany(
			mappedBy = "release",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<Track> tracks = new ArrayList<>();

	@OneToMany(
			mappedBy = "album",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<Image> images = new ArrayList<>();

	@ManyToMany
	@JoinTable(
			name = "album_users",
			joinColumns = @JoinColumn(name = "album_id"),
			inverseJoinColumns = @JoinColumn(name = "users_id")
	)
	private Set<User> users = new HashSet<>();
}