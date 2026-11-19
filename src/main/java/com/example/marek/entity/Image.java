package com.example.marek.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "image")
@Getter
@Setter
@NoArgsConstructor
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;
	private String uri;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id", nullable = false)
	private Album album;
}