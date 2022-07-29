package com.mensworld.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mensworld.dao.CategoryRepository;
import com.mensworld.dao.ShopOwnerRepository;
import com.mensworld.entities.Category;
import com.mensworld.entities.ShopOwner;

@Controller
@RequestMapping("/shop")
public class ShopOwnerController {
    @Autowired
	private ShopOwnerRepository shopOwnerRepository;
    @Autowired
	private CategoryRepository categoryRepository;
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        String email = principal.getName();
        ShopOwner user = shopOwnerRepository.getUserByEmail(email);
        model.addAttribute("title", "dashboard");
        model.addAttribute("user", user);
		return "shopownerdashboard";
    }
    @GetMapping("/add-product")
	public String addProduct(Model model, Principal principal){
        String email = principal.getName();
        ShopOwner user = shopOwnerRepository.getUserByEmail(email);
        List<Category> catgeories = categoryRepository.findAll();
        model.addAttribute("title", "dashboard");
        model.addAttribute("user", user);
        model.addAttribute("categories", catgeories);
		return "addproduct";
	}
}
