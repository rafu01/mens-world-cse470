package com.mensworld.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.mensworld.dao.CategoryRepository;
import com.mensworld.dao.ProductsRepository;
import com.mensworld.dao.ShopOwnerRepository;
import com.mensworld.dao.ShopRepository;
import com.mensworld.dao.UserRepository;
import com.mensworld.entities.Category;
import com.mensworld.entities.Product;
import com.mensworld.entities.Shop;
import com.mensworld.entities.ShopOwner;
import com.mensworld.entities.User;

@Controller
@RequestMapping("/shop")
public class ShopOwnerController {
    @Autowired
	private ShopOwnerRepository shopOwnerRepository;
    @Autowired
	private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductsRepository productsRepository;
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
        model.addAttribute("title", "add product");
        model.addAttribute("user", user);
        model.addAttribute("categories", catgeories);
		return "addproduct";
	}
    @GetMapping(value = ("/{id}"))
    public String shop(Model model, Principal principal, @PathVariable int id){
        Shop shop = shopRepository.getReferenceById(id);
        model.addAttribute("shop", shop);
        try{
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            model.addAttribute("user", user);
        }
        catch(Exception e){
            
        }
        model.addAttribute("title", shop.getName());
        return "singleshop";
    }
    @GetMapping("/add-items")
    public String add_items(Model model, Principal principal){
        String email = principal.getName();
        ShopOwner user = shopOwnerRepository.getUserByEmail(email);
        Shop shop = user.getShop();
        model.addAttribute("shop", shop);
        model.addAttribute("user", user);
        model.addAttribute("title", "add-items");
        return "add_items";
    }
    @PostMapping(value=("/add-items"))
    public RedirectView save_add_items(Model model, Principal principal,
    @RequestParam("name") String name, @RequestParam("description") String description,
    @RequestParam("price") Integer price, @RequestParam("quantity") Integer quantity,
    @RequestParam("file") MultipartFile file) throws IOException{

        String email = principal.getName();
        ShopOwner user = shopOwnerRepository.getUserByEmail(email);
        Shop shop = user.getShop();
        // List<Product> current_products = shop.getProducts();
        // if(current_products.isEmpty()){
        //     current_products = new ArrayList<Product>();
        // }
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        if(file!=null) {
			byte[] image = java.util.Base64.getEncoder().encode(file.getBytes());
			product.setImage(image);
		}
        productsRepository.save(product);
        shop.getProducts().add(product);
        // shop.setProducts(current_products);
        // productsRepository.save(product);
        shopRepository.save(shop);
        return new RedirectView("/shop/dashboard");
    }
}
