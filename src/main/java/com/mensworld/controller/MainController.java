package com.mensworld.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mensworld.dao.CustomerRepository;
import com.mensworld.entities.Customer;

@Controller
public class MainController {
	@Autowired
	private CustomerRepository customerRepository;
	@GetMapping("/")
	public String home() {
//		User us = new User();
//		System.out.print(us.getId());
//		userRepository.save(us);
		return "home";
	}
	
}
