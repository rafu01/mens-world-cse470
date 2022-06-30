package com.mensworld.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mensworld.dao.CustomerRepository;
import com.mensworld.entities.Customer;
import com.mensworld.utilities.Message;

@Controller
@RequestMapping("/user")
public class CustomerController {
    @Autowired
	private CustomerRepository customerRepository;
    @Autowired
	private BCryptPasswordEncoder passwordEncoder;
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        String email = principal.getName();
        Customer customer = customerRepository.getUserByEmail(email);
        System.out.println(customer.getName());
        model.addAttribute("title", "dashboard");
        model.addAttribute("customer", customer);
		return "dashboard";
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
			session.setAttribute("message",new Message("Successfully registered! ","notification is-success"));
			return "login";
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("customer",customer);
			session.setAttribute("message",new Message(e.getMessage(),"notification is-danger"));
			return "signup";
		}
	}
}
