package com.example.marek.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "box")
@Getter
@Setter
@NoArgsConstructor
public class Box {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private LocalDate date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToMany
	@JoinTable(
			name = "box_albums",
			joinColumns = @JoinColumn(name = "box_id"),
			inverseJoinColumns = @JoinColumn(name = "album_id")
	)
	private Set<Album> albums = new HashSet<>();
}