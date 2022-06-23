package com.mensworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class CustomerConfig extends WebSecurityConfigurerAdapter{
	// @Bean
	// public UserDetailsService getUserDetailService() {
	// 	return new UserDetailsServiceImpl();
	// }
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	// @Bean
	// public DaoAuthenticationProvider authenticationProvide() {
	// 	DaoAuthenticationProvider daoAuthenticationProvider =  new DaoAuthenticationProvider();
	// 	daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
	// 	daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
	// 	return daoAuthenticationProvider;
	// }
	// @Override
	// protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	// 	auth.authenticationProvider(authenticationProvide());
	// }
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// setting up role based permission
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/user/**").hasRole("USER")
		.antMatchers("/**").permitAll().and().formLogin().loginPage("/signin").defaultSuccessUrl("/user/dashboard").and().csrf().disable();
		http.csrf().disable();
	}
}


