package com.mensworld.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.mensworld.dao.AdminRepository;
import com.mensworld.dao.CategoryRepository;
import com.mensworld.dao.CustomerRepository;
import com.mensworld.dao.OrderRepository;
import com.mensworld.dao.ShopRepository;
import com.mensworld.dao.UserRepository;
import com.mensworld.entities.Admin;
import com.mensworld.entities.Category;
import com.mensworld.entities.Customer;
import com.mensworld.entities.Order;
import com.mensworld.entities.Shop;
import com.mensworld.entities.User;
import com.mensworld.utilities.Message;

import net.bytebuddy.dynamic.scaffold.MethodGraph.NodeList;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        String email = principal.getName();
        Admin user = adminRepository.getUserByEmail(email);
        model.addAttribute("title", "admin panel");
        model.addAttribute("user",user);
        return "admindashboard";
    }
    @GetMapping("/add-category")
    public String addCategory(Model model,Principal principal){
        String email = principal.getName();
        Admin user = adminRepository.getUserByEmail(email);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("title", "add category");
        model.addAttribute("categories", categories);
        model.addAttribute("user",user);
        return "addcategory";
    }
    @PostMapping("/add-category")
    public String saveCategory(@RequestParam("name") String name, Model model,Principal principal, HttpSession session){
        String email = principal.getName();
        Admin user = adminRepository.getUserByEmail(email);
        Category category = new Category();
        category.setName(name);
        this.categoryRepository.save(category);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("title", "add category");
        model.addAttribute("user",user);
        model.addAttribute("categories", categories);
        session.setAttribute("message",new Message("category added: "+name,"notification is-success"));
        return "addcategory";
    }
    @GetMapping("/all-shops")
    public String all_shops(Model model, Principal principal){
        String email = principal.getName();
        Admin user = adminRepository.getUserByEmail(email); 
        List<Shop> shops = shopRepository.findAll();
        model.addAttribute("title", "add category");
        model.addAttribute("user",user);
        model.addAttribute("shops", shops);
        return "allshops";
    }
    @GetMapping("/all-customers")
    public String all_customers(Model model, Principal principal){
        String email = principal.getName();
        Admin user = adminRepository.getUserByEmail(email); 
        List<Customer> customers = customerRepository.findAll();
        model.addAttribute("title", "add category");
        model.addAttribute("user",user);
        model.addAttribute("customers", customers);
        return "allcustomers";
    }
    @GetMapping("/pending-shops")
    public String pendingshops(Model model, Principal principal){
        String email = principal.getName();
        Admin user = adminRepository.getUserByEmail(email); 
        List<Shop> shops = shopRepository.findAll();
        model.addAttribute("title", "shop status");
        model.addAttribute("user",user);
        model.addAttribute("shops", shops);
        return "pendingshops";
    }
    @PostMapping(value=("/update_status/{id}"))
    public RedirectView update_status(Model model, Principal principal, @PathVariable int id,@RequestParam("approved_status") String approved_status){
        String email = principal.getName();
        Admin user = adminRepository.getUserByEmail(email); 
        Shop shop = shopRepository.getReferenceById(id);
        shop.setApproved_status(approved_status.equals("True")?true:false);
        shopRepository.save(shop);
        List<Shop> shops = shopRepository.findAll();
        model.addAttribute("title", "shop status");
        model.addAttribute("user",user);
        model.addAttribute("shops", shops);
        return new RedirectView("/admin/pending-shops");
    }
    @GetMapping("/view-order")
    public String view_orders(Model model, Principal principal){
        String email = principal.getName();
        Admin user = adminRepository.getUserByEmail(email); 
        List<Shop> shops = shopRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("title", "orders");
        model.addAttribute("user",user);
        model.addAttribute("orders", orders);
        model.addAttribute("shops", shops);
        return "view_orders";
    }
}
