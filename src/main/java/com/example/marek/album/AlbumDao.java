package com.example.marek.album;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;



@Repository
@Transactional
public class AlbumDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save (Album album) {
		
		entityManager.persist(album);
	}
	
	public void update (Album album) {
		
		entityManager.merge(album);
	}
	
	public void delete (Album album) {
		
		entityManager.remove(entityManager.contains(album) ?
				album : entityManager.merge(album));
	}

}
