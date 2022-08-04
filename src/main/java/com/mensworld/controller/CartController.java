package com.mensworld.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;

import com.mensworld.dao.CartRepository;
import com.mensworld.dao.ProductsRepository;
import com.mensworld.dao.ShopRepository;
import com.mensworld.dao.UserRepository;
import com.mensworld.entities.Cart;
import com.mensworld.entities.Product;

@Controller
public class CartController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CartRepository cartRepository;
    @GetMapping("/add-to-cart/{id}")
    public RedirectView add_to_cart(@PathVariable int id, Model model, Principal principal, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        Product product = productsRepository.getReferenceById(id);
        if(cart==null){
            cart = new Cart(); 
            List<Product> prd = new ArrayList<>();
            prd.add(product);
            cart.setProducts(prd);
            cart.calculateTotal();
            cart.getQuantity();
            session.setAttribute("cart", cart);
            cart.setProducts(new ArrayList<Product>());  
        }
        else{
            cart.getProducts().add(product);
            cart.calculateTotal();
            cart.getQuantity();
        }
        // cartRepository.save(cart);
        // System.out.println(cart.getProducts().size());
        return new RedirectView("/products");
    }
    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        model.addAttribute("cart", cart);
        model.addAttribute("products", cart.getProducts());
        model.addAttribute("title", "carts");
        return "cart";
    }
    @GetMapping("/delete-from-cart/{id}")
    public String delete_from_cart(@PathVariable int id,Model model, Principal principal, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        List<Product> products = cart.getProducts();
        for(int i=0;i<products.size();i++){
            if(products.get(i).getId()==id)
                products.remove(i);
        }
        model.addAttribute("cart", cart);
        model.addAttribute("products", cart.getProducts());
        model.addAttribute("title", "carts");
        return "cart";
    }
}
