package com.mensworld.dao;

import com.mensworld.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
public interface AdminRepository extends JpaRepository<Admin,Integer>{
	@Query("select c from Admin c where c.email = :email")
	public Admin getUserByEmail(@Param("email") String email);
	public Admin findByEmail(String email);
}
