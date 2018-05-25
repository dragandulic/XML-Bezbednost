package com.group4bezbednost.bezbednost.model;

public class UserDTO {
	
	private Long id;
	
	private String email;
	
	private String name;
	
	private String surname;
	
	private String city;
	
	private String phoneNumber;
	
	private String password;
	
	private String newPassword;
	
	private String repeatPassword;
	
	private String role;
	
	private String adminFchangeP;
	
	private int numberlogin;
	
	public UserDTO() {
		
	}
	
	public UserDTO(String email, String name, String surname, String city, String phoneNumber, String Password, String newPassword, String repeatPassword,Long id,String role,String adminFchangep) {
		this.email=email;
		this.name=name;
		this.surname=surname;
		this.city=city;
		this.phoneNumber=phoneNumber;
		this.password=Password;
		this.newPassword=newPassword;
		this.repeatPassword=repeatPassword;
		this.role=role;
		this.adminFchangeP=adminFchangep;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String Password) {
		this.password = Password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAdminFchangep() {
		return adminFchangeP;
	}

	public void setAdminFchangep(String adminFchangep) {
		this.adminFchangeP = adminFchangep;
	}

	public String getAdminFchangeP() {
		return adminFchangeP;
	}

	public void setAdminFchangeP(String adminFchangeP) {
		this.adminFchangeP = adminFchangeP;
	}

	public int getNumberlogin() {
		return numberlogin;
	}

	public void setNumberlogin(int numberlogin) {
		this.numberlogin = numberlogin;
	}
	
	

}

