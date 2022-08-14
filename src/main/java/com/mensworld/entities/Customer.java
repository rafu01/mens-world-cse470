package com.mensworld.entities;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Entity
@Table(name = "customer")
public class Customer extends User{
	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// @Column(name = "id")
	// private int id;
    // public int getId() {
	// 	return id;
	// }
    // public void setId(int id) {
	// 	this.id = id;
	// }
	@OneToMany
	List<Product> favorite;
	@OneToMany
	List<Order> orders;
	public List<Product> getFavorite(){
		return this.favorite;
	}
	public void setFavorite(List<Product> favorite){
		this.favorite = favorite;
	}
	public List<Order> getOrders(){
		return this.orders;
	}
	public void setOrders(List<Order> orders){
		this.orders = orders;
	}
	public void addFavorite(Product product){
		if(favorite==null){
			favorite = new ArrayList<Product>();
		}
		favorite.add(product);
	}
}
