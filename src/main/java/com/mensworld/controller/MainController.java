package com.mensworld.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.mensworld.dao.CustomerRepository;
import com.mensworld.entities.Customer;

@Controller
public class MainController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private CustomerRepository customerRepository;
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "men's world");
		// Customer us = new Customer();
		// System.out.print(us.getId());
		// customerRepository.save(us);
		return "home";
	}
	@GetMapping("/login")
	public String login(Model model){
		model.addAttribute("title", "login");
		return "login";
	}
	@GetMapping("/signup")
	public String signup(Model model){
		model.addAttribute("title", "signup");
		return "signup";
	}
}
