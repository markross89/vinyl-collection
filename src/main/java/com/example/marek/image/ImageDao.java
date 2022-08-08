package com.example.marek.image;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


@Repository
@Transactional
public class ImageDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save (Image image) {
		
		entityManager.persist(image);
	}
	
	public void update (Image image) {
		
		entityManager.merge(image);
	}
	
	public void delete (Image image) {
		
		entityManager.remove(entityManager.contains(image) ?
				image : entityManager.merge(image));
	}
}
