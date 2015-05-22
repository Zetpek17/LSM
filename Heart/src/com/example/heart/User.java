package com.example.heart;

import java.util.Calendar;

public class User {
	private String name;
	private Calendar dateOfBirth;
	private String sex;
	private String alertNumber;
	
	public User(String name, Calendar dot, String sex, String alertNumber) {
		this.name = name;
		this.dateOfBirth = dot;
		this.sex = sex;
		this.alertNumber = alertNumber;
	}
	
	public int countAge() {
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - this.dateOfBirth.get(Calendar.YEAR);  
		if (today.get(Calendar.MONTH) < this.dateOfBirth.get(Calendar.MONTH)) {
		  age--;
		} else if (today.get(Calendar.MONTH) == this.dateOfBirth.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < this.dateOfBirth.get(Calendar.DAY_OF_MONTH)) {
		  age--;
		}
		return age;
	}
}
