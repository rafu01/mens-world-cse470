package com.mensworld.controller;

import java.lang.reflect.Array;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import com.mensworld.utilities.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.mensworld.dao.CustomerRepository;
import com.mensworld.dao.OrderRepository;
import com.mensworld.dao.ProductsRepository;
import com.mensworld.dao.ShopRepository;
import com.mensworld.dao.UserRepository;
import com.mensworld.entities.Coupon;
import com.mensworld.entities.Customer;
import com.mensworld.entities.Order;
import com.mensworld.entities.Product;
import com.mensworld.entities.Shop;
import com.mensworld.utilities.Cart;
import com.mensworld.utilities.Message;
import com.mensworld.entities.ShopWiseOrder;
import com.mensworld.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
	private CustomerRepository customerRepository;
    // @Autowired
	// private BCryptPasswordEncoder passwordEncoder;
	@Autowired 
	private ProductsRepository productsRepository;
	@Autowired
	private ShopRepository shopRepository;
	@Autowired
	private OrderRepository orderRepository;
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal, HttpSession session){
        String email = principal.getName();
        Customer customer = customerRepository.getUserByEmail(email);
		Cart cart = (Cart)session.getAttribute("cart");
		customerRepository.save(customer);
		List<Order> orders = customer.getOrders();
		model.addAttribute("cart", cart);
        model.addAttribute("title", "dashboard");
		model.addAttribute("orders", orders);
        model.addAttribute("user", customer);
		return "dashboard";
    }
	@GetMapping(value=("/add-favorite/{id}"))
	public RedirectView add_to_favorite(@PathVariable int id, Model model, Principal principal, HttpSession session){
		String email = principal.getName();
		Customer customer =  customerRepository.getUserByEmail(email);
		Product product = productsRepository.getReferenceById(id);
		customer.addFavorite(product);
		Cart cart = (Cart)session.getAttribute("cart");
		customerRepository.save(customer);
		model.addAttribute("cart", cart);
		model.addAttribute("user", customer);
		return new RedirectView("/products");
	}
	@GetMapping("/delete-favorite/{id}")
	public RedirectView view_favorite(@PathVariable int id, Model model, Principal principal,HttpServletRequest request){
		String email = principal.getName();
		Customer customer = customerRepository.getUserByEmail(email);
		List<Product> favorite = customer.getFavorite();
		System.out.println(request.getRequestURI());
		for(Product product: favorite){
			if(product.getId()==id){
				favorite.remove(product);
				break;
			}
		}
		customerRepository.save(customer);
		model.addAttribute("user", customer);
		model.addAttribute("title", "favorite");
		return new RedirectView("/customer/dashboard");
	}
	@GetMapping("/checkout")
	public String checkout(Model model, Principal principal, HttpSession session){
		Customer customer = customerRepository.findByEmail(principal.getName());
		Cart cart = (Cart) session.getAttribute("cart");
		List<Pair> pairs = cart.getProducts();
		cart.getTotal_after_charges();
		model.addAttribute("cart", cart);
		model.addAttribute("pairs", pairs);
		model.addAttribute("title", "checkout");
		model.addAttribute("user",customer);
		return "checkout";
	}
	// @GetMapping("/view-favorite")
	// public String view_favorite(Model model, Principal principal){
	// 	String email = principal.getName();
	// 	Customer customer = customerRepository.getUserByEmail(email);
	// 	List<Product> favorite = customer.getFavorite();
	// 	System.out.println(favorite.size());
	// 	model.addAttribute("user", customer);
	// 	model.addAttribute("favorite", favorite);
	// 	model.addAttribute("title", "favorite");
	// 	return "favorite";
	// }
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
	@PostMapping("/confirm-checkout")
	public String confirm_checkout(@RequestParam String name, @RequestParam String address, @RequestParam String number,  Model model, Principal principal, HttpSession session){
		Customer customer = customerRepository.findByEmail(principal.getName());
		Cart cart = (Cart) session.getAttribute("cart");
		Coupon coupon = cart.getCoupon();
		List<Pair> pairs = cart.getProducts();
		Order order = new Order();
		List<Product> products = new ArrayList<>();
		int quantity = 0;
		double total = cart.getTotal_after_charges();
		HashMap<Integer, List<Product>> shop_product = new HashMap<>();
		HashMap<Integer, Integer> shop_qty = new HashMap<>();
		for(Pair pair: pairs){
			Product product = pair.getProduct();
			product.setQuantity(product.getQuantity()-pair.getQuantity());
			this.productsRepository.save(product);
			int shopID  = product.getShop().getId();
			int qty = pair.getQuantity();
			// for total order
			products.add(product);
			quantity+=qty;

			//creating hashmap value for each key
			List<Product> shop_product_list = shop_product.get(shopID);;
			// if key doesnt exist set new list
			if(shop_product_list==null){
				shop_product_list = new ArrayList<>();
			}
			// add product to value in hashmap
			shop_product_list.add(product);
			shop_product.put(shopID, shop_product_list);
			if(shop_qty.get(shopID)==null)
				shop_qty.put(shopID, qty);
			else shop_qty.put(shopID, shop_qty.get(shopID)+qty);
		}
		for(int shopID: shop_product.keySet()){
			ShopWiseOrder shop_wise_order = new ShopWiseOrder();
			Shop shop = shopRepository.getReferenceById(shopID);
			shop_wise_order.setProducts(shop_product.get(shopID));
			shop_wise_order.setQuantity(shop_qty.get(shopID));
			List<ShopWiseOrder> current_orders = shop.getOrders();
			if(current_orders==null){
				current_orders = new ArrayList<>();
			}
			int total_shop_wise = 0;
			for(Product product: shop_product.get(shopID)){
				total_shop_wise+=product.getPrice();
			}
			if(coupon!=null){
				List<Coupon> shop_coupons = shop.getCoupons();
				for(Coupon coup: shop_coupons){
					if(coup.getName().equals(coupon.getName())){
						total_shop_wise -= total_shop_wise*coupon.getPercentage();
						break;
					}
				}
			}
			shop_wise_order.setTotal(total_shop_wise);
			shop_wise_order.setAddress(address);
			shop_wise_order.setName(name);
			shop_wise_order.setNumber(number);
			current_orders.add(shop_wise_order);
		}
		List<Order> customer_orders = customer.getOrders();
		if(customer_orders==null){
			customer_orders = new ArrayList<>();
		}
		customer_orders.add(order);
		order.setProducts(products);
		order.setQuantity(quantity);
		order.setTotal(total);
		order.setAddress(address);
		order.setName(name);
		order.setNumber(number);
		orderRepository.save(order);
		session.removeAttribute("cart");
		// model.addAttribute("cart", cart);
		model.addAttribute("title", "confirmed");
		model.addAttribute("user",customer);
		return "order_confirm";
	} 
}
