package com.mensworld.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mensworld.dao.AdminRepository;
import com.mensworld.dao.CategoryRepository;
import com.mensworld.dao.CustomerRepository;
import com.mensworld.dao.ProductsRepository;
import com.mensworld.dao.ShopOwnerRepository;
import com.mensworld.dao.ShopRepository;
import com.mensworld.dao.UserRepository;
import com.mensworld.entities.Admin;
import com.mensworld.entities.Category;
import com.mensworld.entities.Customer;
import com.mensworld.entities.Product;
import com.mensworld.entities.Shop;
import com.mensworld.entities.ShopOwner;
import com.mensworld.entities.User;
import com.mensworld.utilities.Cart;
import com.mensworld.utilities.Message;

@Controller
public class MainController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ProductsRepository productsRepository;
	@Autowired
	private ShopOwnerRepository shopownerRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired 
	private ShopRepository shopRepository;
	@Autowired
	private AdminRepository adminRepository;
	@GetMapping("/")
	public String home(Model model, Principal principal, HttpSession session) {
		model.addAttribute("title", "men's world");
		try{
			String email = principal.getName();
			User user = userRepository.getUserByEmail(email);
			model.addAttribute("user", user);
		}
		catch(Exception e){

		}
		List<Product> products;
		if(productsRepository.findAll().size()>7)
			products = productsRepository.findAll().subList(0, 8);
		else
			products = productsRepository.findAll();
		Cart cart = (Cart) session.getAttribute("cart");
		model.addAttribute("cart", cart);
		model.addAttribute("products", products);
		List<Product> featured_products = new ArrayList<>();
		
		for(Category cat:categoryRepository.findAll()){
			for(Product product: products){
				if(product.getCategory().getName().equals(cat.getName())){
					featured_products.add(product);
					break;
				}

			}
		}
		model.addAttribute("featured_products", featured_products);
		model.addAttribute("title", "Men's world");
		// Customer us = new Customer();
		// System.out.print(us.getId());
		// customerRepository.save(us);
		return "home";
	}
	public Object isLogged(Principal principal){
		try{
			String email = principal.getName();
			User user = userRepository.getUserByEmail(email);
			return user;
		}
		catch(Exception e){
			// System.out.println(e);
			return null;
		}
	}
	@GetMapping("/login")
	public String login(Model model, Principal principal){
		if (principal!=null) {
			return "redirect:/";
		}
		model.addAttribute("title","login");
		return "login";
		// if(isLogged(principal)==null){
		// 	model.addAttribute("title", "login");
		// 	return new RedirectView("login");
		// }
		// else{
		// 	String email = principal.getName();
        // 	Customer customer = customerRepository.getUserByEmail(email);
        // 	System.out.println(customer.getName());
        // 	model.addAttribute("title", "dashboard");
        // 	model.addAttribute("customer", customer);
		// 	return new RedirectView("dashboard");
		// }
	}
	
	@GetMapping("/login-error")
	public String login_fail(Model model,HttpSession session){
		model.addAttribute("title","login");
		session.setAttribute("message",new Message("email/password incorrect","notification is-danger"));
		return "login";
	}
	@GetMapping(value = ("/signup/{type}"))
	public String signup(@PathVariable String type, Model model){
		model.addAttribute("title", "signup");
		model.addAttribute("type", type);
		return "signup";
	}
	@RequestMapping(value = ("/signup/{type}"), method=RequestMethod.POST)
	private RedirectView ProcessSignup(@RequestParam("fullname") String fullname, @RequestParam("email") String email,
			@RequestParam("password") String password,@PathVariable String type, Model model,
			HttpSession session) {
		User user;
		if(type.equals("ROLE_SHOP")){
			user = new ShopOwner();
		}
		else if(type.equals("ROLE_CUSTOMER")){
			user = new Customer();
		}
		else{
			user = new Admin();
		}
		user.setName(fullname);
		user.setEmail(email);
		try {
			if(customerRepository.getUserByEmail(email)!=null) {
				throw new Exception("user email already exists");
			}
			if(password.length()<6) {
				throw new Exception("password length must be at least 6");
			}
			user.setPassword(passwordEncoder.encode(password));
			user.setRole(type);
			if(type.equals("ROLE_SHOP")){
				Shop shop = new Shop();
				ShopOwner shopowner = (ShopOwner)user;
				shop.setApproved_status(true);
				shopowner.setShop(shop);
				this.shopownerRepository.save(shopowner);
			}
			else if(type.equals("ROLE_CUSTOMER")){
				this.customerRepository.save((Customer)user);
			} 
			else{
				this.adminRepository.save((Admin)user);
			}
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("Successfully registered! ","notification is-success"));
			return new RedirectView("/login");
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message(e.getMessage(),"notification is-danger"));
			return new RedirectView("/signup/"+type);
		}
	}
	@GetMapping("/selectsignup")
	public String selectSignup(Model model){
		model.addAttribute("title", "Select Option");
		return "selectsignup";
	}
	@GetMapping("/products")
	public String products(Model model, Principal principal, HttpSession session){
		// Product p = createProd();
		// productsRepository.save(p);
		List<Product> allProduct = productsRepository.findAll();
		// Product x = productsRepository.getReferenceById(36);
		// x.setName(id);
		Object user = isLogged(principal);
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("user", user);
		model.addAttribute("title", "products");
		model.addAttribute("products", allProduct);
		// System.out.println(allProduct);
		Cart cart =(Cart) session.getAttribute("cart");
		model.addAttribute("cart",cart);
		return "products";
	}
	@PostMapping(value=("/products"))
	public String search_products(Model model, Principal principal, @RequestParam(required = false) String name,
	@RequestParam(required = false) String sort, @RequestParam(required = false) String search_type, @RequestParam(required = false) String category){
		List<Product> allProduct = productsRepository.findAll();
		Object user = isLogged(principal);
		// System.out.println(name);
		if(search_type.equals("Search Product")){
			List<Product> query_product = new ArrayList<Product>();
			if(name == null && category==null){
				query_product = productsRepository.findAll();
			}
			else if(!name.equals("")){
				// System.out.println("this");
				for (Product product : allProduct) {
					if(product.getName().toLowerCase().contains(name.toLowerCase())){
						query_product.add(product);
					}
				}
			}
			else if(category!=null){
				for (Product product : allProduct) {
					if(product.getCategory().getName().equals(category)) {
						// System.out.println(product.getName());
						query_product.add(product);
					}
				}
			}
			if(sort != null){
				if(sort.equals("Low"))
					query_product = sort_by_price_asc(query_product);
				else query_product = sort_by_price_desc(query_product);
			}
			model.addAttribute("products", query_product);
		}
		else{
			List<Shop> allshops =shopRepository.findAll();
			List<Shop> query_shop = new ArrayList<Shop>();
			for(Shop shop:allshops){
				if(shop.getName()!=null && shop.getName().toLowerCase().contains(name.toLowerCase())){
					query_shop.add(shop);
				}
			}
			model.addAttribute("shops", query_shop);
		}
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("user", user);
		model.addAttribute("title", "search");
		// System.out.println(allProduct);
		return "products";
	}
	public List<Product> sort_by_price_asc(List<Product> products){
		products.sort((p1,p2)->p1.getPrice().compareTo(p2.getPrice()));
		// p.setName("shoe#"+4);
		// p.setPrice("300");
		// p.setQuantity("10");
		// for(Product product: products)
		// 	System.out.println(product.getPrice());
		return products;
	}
	public List<Product> sort_by_price_desc(List<Product> products){
		products.sort((p1,p2)->{return (p2.getPrice()-p1.getPrice());});
		// p.setName("shoe#"+4);
		// p.setPrice("300");
		// p.setQuantity("10");
		// for(Product product: products)
		// 	System.out.println(product.getPrice());
		return products;
	}
	@GetMapping(value = ("/product/{id}"))
	public String singleProduct(@PathVariable int id, Model model, Principal principal, HttpSession session){
		Product product = productsRepository.getReferenceById(id);
		Object customer = isLogged(principal);
		List<Product> products = product.getShop().getProducts();
		products.remove(product);
		Cart cart = (Cart) session.getAttribute("cart");
		model.addAttribute("cart", cart);
		model.addAttribute("user", customer);
		model.addAttribute("products", products);
		model.addAttribute("product",product);
		return "singleproduct";
	}
	@GetMapping("/shops")
	public String shops(Model model, Principal principal){
		Object user = isLogged(principal);
		List<Shop> shops = shopRepository.findAll();
		model.addAttribute("shops", shops);
		model.addAttribute("user", user);
		model.addAttribute("title", shops);
		return "shops";
	}
	@GetMapping(value = ("/shops/{id}"))
	public String singleShop(@PathVariable int id, Model model, Principal principal, HttpSession session){
		Shop shop = shopRepository.getReferenceById(id);
		Object customer = isLogged(principal);
		// Cart cart = (Cart) session.getAttribute("cart");
		// model.addAttribute("cart", cart);
		List<Product> products = shop.getProducts();
		// System.out.println(shop.getName());
		model.addAttribute("user", customer);
		model.addAttribute("shop",shop);
		model.addAttribute("products", products);
		model.addAttribute("title","shop");
		return "singleshop";
	}
}
