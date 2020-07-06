package com.vti.dto;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RegisterRequest {
	private String username;
	private String password;
	private String email;
	
	
	
	
}
