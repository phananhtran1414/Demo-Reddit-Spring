package com.vti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/api/auth/**")
				.permitAll()
				.anyRequest()
				.authenticated();
		
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//
//		return super.authenticationManagerBean();
//	}
//	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		// truền UserDetailsService gồm UserDetailsRepo vào để kiểm tra xác thực người dùng + encoder
//		auth.userDetailsService(service).passwordEncoder(passwordEncoder());
//		
//	}
//	
//	
//	// Phải có nếu ko sẽ có lỗi http ko nhận đường link từ cổng 4200
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		
//		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
//	}
//	
}
