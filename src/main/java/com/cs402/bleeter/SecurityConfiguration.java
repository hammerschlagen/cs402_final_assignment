package com.cs402.bleeter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.cs402.bleeter.users.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {	
	@Autowired
	private UserService userService;
	
	@Autowired
	public void configAuthBuilder(AuthenticationManagerBuilder authBuilder) throws Exception {
		authBuilder.userDetailsService(userService);
	}	
	
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("images/**,fonts/**,css/**,js/**,jsp/**,views/login.jsp,/login").permitAll()
				.antMatchers("/bleets").authenticated()
				.and()
			.formLogin()
				.loginPage("/login.jsp")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/")		
				.failureUrl("/login.jsp?error=true")
				.usernameParameter("username")
				.passwordParameter("password")
				.permitAll()
		.and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login.jsp?logout");
	}
}