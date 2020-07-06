package com.vti.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vti.dto.RegisterRequest;
import com.vti.model.User;
import com.vti.model.VerificationToken;
import com.vti.repository.UserRepository;
import com.vti.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	   	private final PasswordEncoder passwordEncoder;
		
	   	private final UserRepository userRepository;
	   	
	   	private final VerificationTokenRepository verificationTokenRepository;
	   	
	   	
	   	
		@Transactional
	    public void signup(RegisterRequest registerRequest) {
	        User user = new User();
	        user.setUsername(registerRequest.getUsername());
	        user.setEmail(registerRequest.getEmail());
	        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
	        user.setCreated(Instant.now());
	        user.setEnabled(false);

	        userRepository.save(user);

	        String token = generateVerificationToken(user);
	        mailService.sendMail(new NotificationEmail("Please Activate your Account",
	                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
	                "please click on the below url to activate your account : " +
	                "http://localhost:8080/api/auth/accountVerification/" + token));
	    }
		
		private String generateVerificationToken(User user) {
	        String token = UUID.randomUUID().toString();
	        VerificationToken verificationToken = new VerificationToken();
	        verificationToken.setToken(token);
	        verificationToken.setUser(user);

	        verificationTokenRepository.save(verificationToken);
	        return token;
	    }
}