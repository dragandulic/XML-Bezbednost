package com.group4bezbednost.bezbednost.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4bezbednost.bezbednost.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	
	User findByEmailEqualsAndPasswordEquals(String email, String password);

	User findByEmailEquals(String email);
	
	User findByIdEquals(Long id);
	
	List<User> findByRoleEquals(String s);
}
