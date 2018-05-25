package com.group4bezbednost.bezbednost.service;

import java.util.List;
import java.util.Set;

import com.group4bezbednost.bezbednost.model.User;
import com.group4bezbednost.bezbednost.model.UserDTO;



public interface UserServiceInterface {
	
	UserDTO findUser (String email, String password);
	
	User findUserEdit (UserDTO u);

	User saveUser(UserDTO u);
	
	User acceptRegistration(Long id);
	
	User findUserPassword(UserDTO u);


	
	UserDTO addAdministrator(User u);
	
	List<User> getAdminOfCT();
	
	UserDTO getUserid(Long id);
	
	List<User> getUsersFromUser(Long id);
}
