package com.vti.dto;


import javax.persistence.Entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LoginRequest {
	private String username;
	private String password;
	
	

	
}
