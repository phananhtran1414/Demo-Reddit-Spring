package com.vti.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vti.dto.RegisterRequest;
import com.vti.service.AuthService;

@RestController
@RequestMapping(name = "/api/auth")
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping("/sign-up")
	public ResponseEntity<?> signup(@RequestBody RegisterRequest registerRequest) {
		
		authService.signup(registerRequest);
		
		return ResponseEntity.ok("");
	}
	
}
