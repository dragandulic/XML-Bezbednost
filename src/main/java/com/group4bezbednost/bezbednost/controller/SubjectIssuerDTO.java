package com.group4bezbednost.bezbednost.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubjectIssuerDTO {

	
	private	String name;  //John
	private	String surname;  //Jackson
	private	String organization; // A-Company
	private String state; //RS
	
	private String email; //acompany@gmail.com
	private String serialnumber;
	private	String optionalCompanyName;
	private	String startDate;
	private String password;
	private	String endDate;
	
	
	
	
	/*
{
  "name": "marko",
  "surname": "juric",                       POSTMAN / SIGNED
  "organization": "acompany",
  "state": "ns",
  "email": "msa@gmail.com",
  "password": "ev",
  "serialnumber": "867887",
  "optionalCompanyName": "acomp",
  "startDate": "2017-12-31",
  "endDate": "2018-12-31",
  "issueralias": "acompan",
  "issuerpassword": "evegr"

}

	 */
	
	
	
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
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	public String getOptionalCompanyName() {
		return optionalCompanyName;
	}
	public void setOptionalCompanyName(String optionalCompanyName) {
		this.optionalCompanyName = optionalCompanyName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
	
	
	
}
