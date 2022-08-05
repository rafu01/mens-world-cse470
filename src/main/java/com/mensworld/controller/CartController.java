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

// import com.mensworld.dao.CartRepository;
import com.mensworld.dao.ProductsRepository;
import com.mensworld.dao.ShopRepository;
import com.mensworld.dao.UserRepository;
import com.mensworld.entities.Product;
import com.mensworld.utilities.Cart;
import com.mensworld.utilities.Pair;

@Controller
public class CartController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductsRepository productsRepository;
    // @Autowired
    // private CartRepository cartRepository;
    @GetMapping("/add-to-cart/{id}")
    public RedirectView add_to_cart(@PathVariable int id, Model model, Principal principal, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        Product product = productsRepository.getReferenceById(id);
        if(cart==null){
            cart = new Cart(); 
            List<Pair> pairs = new ArrayList<>();
            Pair pair = new Pair();
            pair.setProduct(product);
            pair.setQuantity(1);
            pairs.add(pair);
            cart.setProducts(pairs);
            // cart.setProducts(prd);
            cart.calculateTotal();
            cart.getQuantity();
            session.setAttribute("cart", cart);
            // cart.setProducts(new ArrayList<Product>());  
        }
        else{
            // cart.getProducts().add(product);
            boolean found = false;
            for (Pair pair : cart.getProducts()) {
                if(pair.getProduct().getId()==product.getId()){
                    pair.setQuantity(pair.getQuantity()+1);
                    found = true;
                    break;
                }
            }
            if(found==false){
                Pair pair = new Pair();
                pair.setProduct(product);
                pair.setQuantity(1);
                cart.getProducts().add(pair);
            }
            cart.calculateTotal();
            cart.getQuantity();
            
        }
        session.setAttribute("cart", cart);
        // cartRepository.save(cart);
        // System.out.println(cart.getProducts().size());
        return new RedirectView("/products");
    }
    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        model.addAttribute("cart", cart);
        
        if(cart!=null){
            List<Pair> pairs = cart.getProducts();
            model.addAttribute("pairs", pairs);
        }
        model.addAttribute("title", "carts");
        return "cart";
    }
    @GetMapping("/delete-from-cart/{id}")
    public String delete_from_cart(@PathVariable int id,Model model, Principal principal, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        List<Pair> pairs = cart.getProducts();
        for(int i=0;i<pairs.size();i++){
            if(pairs.get(i).getProduct().getId()==id){
                pairs.get(i).setProduct(null);
                pairs.remove(i);
                // break;
            }
        }
        pairs = cart.getProducts();
        // List<Product> products = new ArrayList<>();
        // for(Pair pair: cart.getProducts()){
        //     products.add(pair.getProduct());
        // }
        model.addAttribute("cart", cart);
        model.addAttribute("pairs", pairs);
        model.addAttribute("title", "carts");
        return "cart";
    }
}
