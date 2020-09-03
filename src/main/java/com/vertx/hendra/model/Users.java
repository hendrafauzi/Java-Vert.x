package com.vertx.hendra.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Users {
	public static final AtomicInteger COUNTER = new AtomicInteger();
	
	public final int id;
	public String firstName;
	public String lastName;
	public int age;
	public String email;
	public boolean isMarried;
	
	public Users(String firstName, String lastName, int age, String email) {
		this.id = COUNTER.getAndIncrement();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.email = email;
		this.isMarried = isMarried;
	}

	public int getId() {
		return id;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isMarried() {
		return getAge() > 18;
	}

	public void setMarried(boolean isMarried) {
		this.isMarried = isMarried;
	}
}