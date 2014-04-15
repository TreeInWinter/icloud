package com.icloud.stock.business.impl;

import org.springframework.stereotype.Service;

import com.icloud.stock.business.PersonService;

@Service("personService")
public class PersonServiceImpl implements PersonService {

	@Override
	public int save(String name) {
		System.out.println("我是save()方法");
		return 0;
	}

	@Override
	public void update(String name, Integer id) {
		// TODO Auto-generated method stub
		System.out.println("我是update()方法");
	}

	@Override
	public String getPersonName(Integer id) {
		// TODO Auto-generated method stub
		System.out.println("我是getPersonName()方法");
		return null;
	}

}
