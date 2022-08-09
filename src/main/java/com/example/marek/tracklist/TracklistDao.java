package com.example.marek.tracklist;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


@Repository
@Transactional
public class TracklistDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save (Tracklist tracklist) {
		
		entityManager.persist(tracklist);
	}
	
	public void update (Tracklist tracklist) {
		
		entityManager.merge(tracklist);
	}
	
	public void delete (Tracklist tracklist) {
		
		entityManager.remove(entityManager.contains(tracklist) ?
				tracklist : entityManager.merge(tracklist));
	}
}