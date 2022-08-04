package com.mensworld.entities;

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

@Entity
@Table(name = "cart")
public class Cart {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	private String name;
    private int quantity;
    @OneToMany(cascade = CascadeType.ALL,fetch =FetchType.LAZY)
    private List<Product> products;
	private int total;
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public int getQuantity() {
		quantity = products.size();
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    public List<Product> getProducts() {
		return this.products;
	}
	public void setProducts(List<Product> products) {
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
        for (Product product : products) {
            temp_total+=product.getPrice();
        }
        return temp_total;
    }
}
