package com.group4bezbednost.bezbednost.controller;



import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		
		
		
		
		return new MessageResponseDTO("success self signed");
	}
	
	
	@PostMapping("/signed")
	public MessageResponseDTO createSignedCertificate(@RequestBody SubjectIssuerDTO input){
		
		
		
		certificateService.signedCertificate(input);
		
		
		return new MessageResponseDTO("success signed");
	}
	
	
	
	@GetMapping("getCertificate/{id}")
	public MessageResponseDTO getCertificateBySerial(@PathVariable String id){
		
		X509Certificate cert = certificateService.getCertificate(id);
		
		
		System.out.println(cert);
		
		return new MessageResponseDTO("Successfully get certificate");
	}
	
	
	
	
	
}
