package com.group4bezbednost.bezbednost.controller;



import javax.security.cert.Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.group4bezbednost.bezbednost.service.CertificateService;



@RestController
@RequestMapping("/certificates")
public class CertificateController {

	@Autowired
	private CertificateService certificateService;
	
	
	
	
	@PostMapping("/selfsigned")
	public MessageResponseDTO createSelfSignedCertificate(@RequestBody SubjectDTO input){
		
		
	
		
		certificateService.selfSignedCertificate(input);
		
		
		
		
		return new MessageResponseDTO("success");
	}
	
	
}
