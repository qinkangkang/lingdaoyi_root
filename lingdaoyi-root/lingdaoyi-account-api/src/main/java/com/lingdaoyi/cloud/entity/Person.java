package com.lingdaoyi.cloud.entity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "person")
public class Person extends BaseEntity {

	@Column
	private String name;

	@Column
	private Integer age;

	public String getName() {
		return name;
	}

	public Person(String name, Integer age) {
		super();
		// this.id = id;
		this.name = name;
		this.age = age;
	}

	public Person() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
