package com.group4bezbednost.bezbednost.service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.group4bezbednost.bezbednost.model.User;

@Service
public class EmailService{

	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private Environment env;
	
	
	
	public void sendEmailToUser(User user) throws MailException, InterruptedException {
		
		System.out.println("Slanje emaila...");
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("spring.mail.username"));
		email.setSubject("Potvrda registracije");
		String text = "http://localhost:8085/user/acceptRegist/"+user.getId();
		email.setText(text);
		
		javaMailSender.send(email);
		
		System.out.println("email poslat");
	}
	
	
	




	
}
