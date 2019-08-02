package com.example.template;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Customer {
	String type ;
	String stateMessage = "We found a customer violating against our policy";
	String email;
	String addr;
	String phone;
	String customerName = "Song Jun";
	int birthday;
	boolean gender;
	boolean blacklistcheck;

	
	
	
}
