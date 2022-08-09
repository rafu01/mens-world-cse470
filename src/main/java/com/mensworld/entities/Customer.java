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
	
	public List<Product> getFavorite(){
		return this.favorite;
	}
	public void setFavorite(List<Product> favorite){
		this.favorite = favorite;
	}
	public void addFavorite(Product product){
		if(favorite==null){
			favorite = new ArrayList<Product>();
		}
		favorite.add(product);
	}
}
