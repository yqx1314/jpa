package com.suning.jpa.service;

import com.suning.jpa.dao.PersonDao;
import com.suning.jpa.spring.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author yqx
 */
@Service
public class PersonService {
	
	@Autowired
	private PersonDao personDao;
	
	@Transactional(rollbackFor=Exception.class)
	public void savePersons(Person p1, Person p2){
		personDao.save(p1);
		
//		int i = 10 / 0;
		
		personDao.save(p2);
	}
	
}
