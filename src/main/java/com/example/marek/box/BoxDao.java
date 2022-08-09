package com.example.marek.box;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


@Repository
@Transactional
public class BoxDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save (Box box) {
		
		entityManager.persist(box);
	}
	
	public void update (Box box) {
		
		entityManager.merge(box);
	}
	
	public void delete (Box box) {
		
		entityManager.remove(entityManager.contains(box) ?
				box : entityManager.merge(box));
	}
}
