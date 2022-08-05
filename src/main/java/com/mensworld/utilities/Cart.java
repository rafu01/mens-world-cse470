package com.mensworld.utilities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mensworld.entities.Product;

// @Entity
// @Table(name = "cart")
public class Cart {
    // @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// @Column(name = "id")
	// private int id;
	private String name;
    private int quantity;
    // @OneToMany(cascade = CascadeType.ALL,fetch =FetchType.LAZY)
    private List<Pair> products;
	private int total;
    // public int getId() {
	// 	return id;
	// }
	// public void setId(int id) {
	// 	this.id = id;
	// }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public int getQuantity() {
		this.quantity=0;
		for(Pair pair:products){
			this.quantity+=pair.getQuantity();
		}
		return this.quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    public List<Pair> getProducts() {
		return this.products;
	}
	public void setProducts(List<Pair> products) {
		this.products = products;
	}
	public int getTotal() {
        total = calculateTotal();
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
    public int calculateTotal(){
        int temp_total = 0;
        for (Pair pair : products) {
			Product product = pair.getProduct();
            temp_total+=product.getPrice();
        }
        return temp_total;
    }
}
