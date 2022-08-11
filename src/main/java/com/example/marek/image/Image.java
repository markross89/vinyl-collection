package com.example.marek.image;

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
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String type;
	private String uri;



}
