package com.mensworld.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mensworld.dao.CustomerRepository;
import com.mensworld.entities.Customer;


public class CustomerDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private CustomerRepository customerRepository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer =customerRepository.getUserByEmail(email);
		if(customer==null) {
			customer = customerRepository.findByEmail(email);
			if(customer==null)
				throw new UsernameNotFoundException("could not find user");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(customer);
		return customUserDetails;
	}


}