package com.aksakalmustafa.genericdao.model;

import javax.persistence.Entity;

@Entity
public class Department extends BaseEntity {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Department [name=" + getName() + ", id=" + getId() + "]";
	}

}
