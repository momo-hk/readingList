package com.momo.springboot.readinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	ReaderRepository readerRepository;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(useDetailsService());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.formLogin()
				.loginPage("/login")
				.failureUrl("/login?error=true")
				.permitAll()
			.and()
			.authorizeRequests()
				.antMatchers("/").hasRole("READER")
				.antMatchers("/mgmt/**").hasRole("READER")
				.antMatchers("/**").permitAll()
			.and()
			.httpBasic();
	}
	
	@Bean
	public UserDetailsService useDetailsService() {
		return name -> readerRepository.findOne(name);
	}
}
