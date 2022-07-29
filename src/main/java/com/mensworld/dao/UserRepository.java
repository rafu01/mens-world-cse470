package com.mensworld.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mensworld.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    @Query("select c from User c where c.email = :email")
	public User getUserByEmail(@Param("email") String email);
    public User findByEmail(String email);
}