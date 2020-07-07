package com.vti.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vti.dto.AuthenticationResponse;
import com.vti.dto.LoginRequest;
import com.vti.dto.RegisterRequest;
import com.vti.service.AuthService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/sign-up")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		
		authService.signup(registerRequest);
		
		return new ResponseEntity<>("User Registration Successful" , HttpStatus.OK);
	}
	
	@GetMapping("/accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token){
		
		authService.verifyAccount(token);
		
		return new ResponseEntity<>("Account Activated Success",HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		
		return authService.login(loginRequest);
		
	}
	
//	@GetMapping("/hello")
//	public String home() {
//		
//		return "12312313121";
//	}
	
	
}
