package com.mensworld.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.mensworld.dao.CustomerRepository;
import com.mensworld.dao.ProductsRepository;
import com.mensworld.entities.Customer;
import com.mensworld.entities.Product;
import com.mensworld.utilities.Message;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
	private CustomerRepository customerRepository;
    // @Autowired
	// private BCryptPasswordEncoder passwordEncoder;
	@Autowired 
	private ProductsRepository productsRepository;
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        String email = principal.getName();
        Customer customer = customerRepository.getUserByEmail(email);
        model.addAttribute("title", "dashboard");
        model.addAttribute("user", customer);
		return "dashboard";
    }
	@GetMapping(value=("/add-favorite/{id}"))
	public RedirectView add_to_favorite(@PathVariable int id, Model model, Principal principal){
		String email = principal.getName();
		Customer customer =  customerRepository.getUserByEmail(email);
		Product product = productsRepository.getReferenceById(id);
		customer.addFavorite(product);
		model.addAttribute("user", customer);
		return new RedirectView("/products");
	}
	@GetMapping("/view-favorite")
	public String view_favorite(Model model, Principal principal){
		String email = principal.getName();
		Customer customer = customerRepository.getUserByEmail(email);
		List<Product> favorite = customer.getFavorite();
		System.out.println(favorite.size());
		model.addAttribute("user", customer);
		model.addAttribute("favorite", favorite);
		model.addAttribute("title", "favorite");
		return "favorite";
	}
    // @RequestMapping(path="/signup", method=RequestMethod.POST)
	// private String ProcessSignup(@RequestParam("fullname") String fullname, @RequestParam("email") String email,
	// 		@RequestParam("password") String password,Model model,
	// 		HttpSession session) {
	// 	Customer customer = new Customer();
	// 	customer.setName(fullname);
	// 	customer.setEmail(email);
	// 	customer.setRole("ROLE_ADMIN");
	// 	try {
	// 		if(customerRepository.getUserByEmail(email)!=null) {
	// 			throw new Exception("user email already exists");
	// 		}
	// 		if(password.length()<6) {
	// 			throw new Exception("password length must be at least 6");
	// 		}
	// 		customer.setPassword(passwordEncoder.encode(password));
	// 		this.customerRepository.save(customer);
	// 		model.addAttribute("customer",customer);
	// 		session.setAttribute("message",new Message("Successfully registered! ","notification is-success"));
	// 		return "login";
	// 	}
	// 	catch(Exception e) {
	// 		e.printStackTrace();
	// 		model.addAttribute("customer",customer);
	// 		session.setAttribute("message",new Message(e.getMessage(),"notification is-danger"));
	// 		return "signup";
	// 	}
	// }
}
