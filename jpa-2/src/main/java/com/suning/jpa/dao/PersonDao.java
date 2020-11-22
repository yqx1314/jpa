package com.suning.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.suning.jpa.spring.entities.Person;
import org.springframework.stereotype.Repository;



/**
 * @author yqx
 */
@Repository
public class PersonDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Person person){
		entityManager.persist(person);
	}
	
}
