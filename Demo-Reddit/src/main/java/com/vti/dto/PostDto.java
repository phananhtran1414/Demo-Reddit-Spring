package com.vti.dto;



import javax.persistence.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PostDto {
	private Long id;
	private String content;
	private String title;
	private String username;
	
	
	
	
	
}
