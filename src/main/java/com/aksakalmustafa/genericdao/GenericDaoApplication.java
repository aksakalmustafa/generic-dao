package com.aksakalmustafa.genericdao;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aksakalmustafa.genericdao.dao.BaseDao;
import com.aksakalmustafa.genericdao.model.Department;
import com.aksakalmustafa.genericdao.model.Person;

@SpringBootApplication
public class GenericDaoApplication implements CommandLineRunner {

	@Autowired
	BaseDao<Person> personDao;

	@Autowired
	BaseDao<Department> departmentDao;

	public static void main(String[] args) {
		SpringApplication.run(GenericDaoApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		Department department = new Department();
		department.setName("department");
		departmentDao.save(department);

		Person person = new Person();
		person.setDepartment(department);
		person.setName("Person1");
		personDao.save(person);

		System.out.println(departmentDao.findById(department.getId()));
		System.out.println(personDao.findById(person.getId()));

	}

}
