package com.example.template;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Customer {
	long id;
	String email;
	String addr;
	String phone;
	String name;
	int birthday;
	boolean gender;
	boolean blacklistcheck;
	
	
	
	
}
