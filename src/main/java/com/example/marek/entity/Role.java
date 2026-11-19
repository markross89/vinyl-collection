package com.example.marek.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
		name = "roles",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "name")
		}
)
@Getter
@Setter
@NoArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;
}