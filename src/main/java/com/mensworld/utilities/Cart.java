package com.mensworld.utilities;

import java.util.ArrayList;
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

import com.mensworld.entities.Coupon;
import com.mensworld.entities.Product;
import com.mensworld.entities.Shop;

// @Entity
// @Table(name = "cart")
public class Cart {
	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// @Column(name = "id")
	// private int id;
	private double tax = 0.15;
	private double delivery_charge = 3;
	private String name;
	private int quantity;
	// @OneToMany(cascade = CascadeType.ALL,fetch =FetchType.LAZY)
	private List<Pair> products;
	private double total;
	private Coupon coupon;

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public double total_after_discount;
	public double total_after_charges;

	public double getTotal_after_discount(Shop shop, List<Pair> products, double discount) {
		total_after_discount = 0;
		System.out.println(total);
		if (shop != null) {
			List<Product> all_product = new ArrayList<>();
			for (Pair pair : products) {
				all_product.add(pair.getProduct());
			}
			for (Product product : all_product) {
				total_after_discount = total - product.getPrice() * (discount / 100);
			}
		}
		return total_after_discount;
	}

	public void setTotal_after_discount(double total_after_discount) {
		this.total_after_discount = total_after_discount;
	}

	public double getTotal_after_charges() {
		total_after_charges = total_after_discount + tax * total_after_discount + delivery_charge;
		return total_after_charges;
	}

	public void setTotal_after_charges(double total_after_charges) {

		this.total_after_charges = total_after_charges;
	}

	// public int getId() {
	// return id;
	// }
	// public void setId(int id) {
	// this.id = id;
	// }
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		this.quantity = 0;
		for (Pair pair : products) {
			this.quantity += pair.getQuantity();
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

	public double getTotal() {
		total = calculateTotal();
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int calculateTotal() {
		int temp_total = 0;
		for (Pair pair : products) {
			Product product = pair.getProduct();
			temp_total += product.getPrice() * pair.getQuantity();
		}
		return temp_total;
	}
}
