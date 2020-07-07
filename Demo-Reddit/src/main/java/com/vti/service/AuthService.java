package com.vti.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vti.dto.AuthenticationResponse;
import com.vti.dto.LoginRequest;
import com.vti.dto.RegisterRequest;
import com.vti.exceptions.SpringRedditException;
import com.vti.model.NotificationEmail;
import com.vti.model.User;
import com.vti.model.VerificationToken;
import com.vti.repository.UserRepository;
import com.vti.repository.VerificationTokenRepository;
import com.vti.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	   	private final PasswordEncoder passwordEncoder;
		
	   	private final UserRepository userRepository;
	   	
	   	private final VerificationTokenRepository verificationTokenRepository;
	   	
	   	private final MailService mailService;
	   	
	   	private final AuthenticationManager authenticationManager;
	   	
	   	private final JwtProvider jwtProvider;
	   	
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

		public void verifyAccount(String token) {
			
			Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
			verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
			
			fetchUserAndEnable(verificationToken.get());
			
		}

		
		@Transactional
		private void fetchUserAndEnable(VerificationToken verificationToken) {
			
			String username = verificationToken.getUser().getUsername();
			User user = userRepository.findByUsername(username)
							.orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
			
			user.setEnabled(true);
			userRepository.save(user);
			
		}

		
		
		public AuthenticationResponse login(LoginRequest loginRequest) {
			
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			String token = jwtProvider.generateToken(authentication);
			
			return new AuthenticationResponse(token , loginRequest.getUsername());
			
		}
}
