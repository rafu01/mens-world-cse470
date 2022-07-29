package com.mensworld.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
}
