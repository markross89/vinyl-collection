package com.example.marek.track;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


@Repository
@Transactional
public class TrackDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save (Track track) {
		
		entityManager.persist(track);
	}
	
	public void update (Track track) {
		
		entityManager.merge(track);
	}
	
	public void delete (Track track) {
		
		entityManager.remove(entityManager.contains(track) ?
				track : entityManager.merge(track));
	}
	
}
