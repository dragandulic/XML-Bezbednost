package com.group4bezbednost.bezbednost.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group4bezbednost.bezbednost.model.User;
import com.group4bezbednost.bezbednost.model.UserDTO;
import com.group4bezbednost.bezbednost.service.EmailService;
import com.group4bezbednost.bezbednost.service.UserService;


@CrossOrigin(origins="http://localhost:4200",allowedHeaders="*")
@RestController
@RequestMapping(value="/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserService userService;
	
	
	/**
	 * SingIn user
	 * @param user
	 * @return user
	 */
	@RequestMapping(value="/logIn",method=RequestMethod.POST)
	public ResponseEntity<UserDTO> singInUser(@RequestBody UserDTO user){
		
		UserDTO user1 = userService.findUser(user.getEmail(), user.getPassword());
		

		if(user1==null) {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user1, HttpStatus.OK);
	
	}
	
	/**
	 * Registration user
	 * @param user
	 * @return user
	 */
	@RequestMapping(value="/registration",method=RequestMethod.PUT)
	public ResponseEntity<User> registrionUser(@RequestBody UserDTO u){
		
		User user1 = userService.saveUser(u);
		if(!(user1==null)) {
			try {
				emailService.sendEmailToUser(user1);
				return new ResponseEntity<>(user1,HttpStatus.OK);
			}catch(Exception e) {
				logger.info("Greska prilikom slanja emaila" + e.getMessage());
			}	
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * The user accepting the registration
	 * @param id of the user
	 * @return
	 */
	@RequestMapping(value="/acceptRegist/{id}")
	public String acceptingRegistration(@PathVariable Long id) {
		User user1 = userService.acceptRegistration(id);
		if(!(user1==null)) {
			return "success";
		}
		return "unsuccess";
	}
	
	/**
	 * Edit user
	 * @param user
	 * @return user
	 */
	@RequestMapping(value="/editUser",method=RequestMethod.POST)
	public ResponseEntity<User> editUser(@RequestBody UserDTO user){
		User u = userService.findUserEdit(user);
		if(u==null) {
			return null;
		}
		
		return new ResponseEntity<>(u,HttpStatus.OK);				
	}
	
	/**
	 * The user change his password
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/editPassword",method=RequestMethod.POST)
	public ResponseEntity<User> editPassword(@RequestBody UserDTO user){
		System.out.println("dosao u edit pass");
		User u= userService.findUserPassword(user);
		if(u==null) {
			return null;
		}
		
		
		return new ResponseEntity<>(u,HttpStatus.OK);

	}
	
	
}
