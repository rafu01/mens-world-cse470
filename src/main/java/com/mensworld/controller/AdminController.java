package com.mensworld.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mensworld.dao.AdminRepository;
import com.mensworld.dao.CategoryRepository;
import com.mensworld.entities.Admin;
import com.mensworld.entities.Category;
import com.mensworld.utilities.Message;

import net.bytebuddy.dynamic.scaffold.MethodGraph.NodeList;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CategoryRepository categoryRepository;
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
        model.addAttribute("title", "add category");
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
        model.addAttribute("title", "add category");
        model.addAttribute("user",user);
        session.setAttribute("message",new Message("category added: "+name,"notification is-success"));
        return "addcategory";
    }
}
