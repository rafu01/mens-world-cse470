package com.mensworld.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mensworld.dao.ShopOwnerRepository;
import com.mensworld.entities.ShopOwner;

@Controller
@RequestMapping("/shop")
public class ShopOwnerController {
    @Autowired
	private ShopOwnerRepository shopOwnerRepository;
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        String email = principal.getName();
        System.out.println(email);
        ShopOwner user = shopOwnerRepository.getUserByEmail(email);
        System.out.println(user);
        model.addAttribute("title", "dashboard");
        model.addAttribute("user", user);
		return "dashboard";
    }
}
