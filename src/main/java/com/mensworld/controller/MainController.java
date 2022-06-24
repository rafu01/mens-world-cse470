package com.mensworld.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.mensworld.dao.CustomerRepository;
import com.mensworld.entities.Customer;
import com.mensworld.utilities.Message;

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
	@RequestMapping(path="/signup", method=RequestMethod.POST)
	private String ProcessSignup(@RequestParam("fullname") String fullname, @RequestParam("email") String email,
			@RequestParam("password") String password,Model model,
			HttpSession session) {
		Customer customer = new Customer();
		customer.setName(fullname);
		customer.setEmail(email);
		try {
			if(customerRepository.getUserByEmail(email)!=null) {
				throw new Exception("user email already exists");
			}
			if(password.length()<6) {
				throw new Exception("password length must be at least 6");
			}
			customer.setPassword(passwordEncoder.encode(password));
			customer.setRole("ROLE_USER");
			this.customerRepository.save(customer);
			model.addAttribute("customer",customer);
			session.setAttribute("message",new Message("Successfully registered! ","alert-success"));
			return "login";
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("customer",customer);
			session.setAttribute("message",new Message(e.getMessage(),"alert-danger"));
			return "signup";
		}
	}
}
