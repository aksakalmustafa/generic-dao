package com.aksakalmustafa.genericdao.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Person extends BaseEntity {

	private String name;

	@ManyToOne
	private Department department;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Person [name=" + getName() + ", department=" + getDepartment().getName() + ", id=" + getId() + "]";
	}

}
