package com.mensworld.dao;

import com.mensworld.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface CustomerRepository extends JpaRepository<Customer,Integer>{
	@Query("select c from Customer c where c.email = :email")
	public Customer getUserByEmail(@Param("email") String email);
}
