package com.group4bezbednost.bezbednost.controller;


import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group4bezbednost.bezbednost.model.SSCertificate;
import com.group4bezbednost.bezbednost.service.CertificateService;


@CrossOrigin(origins="http://localhost:4200",allowedHeaders="*")
@RestController
@RequestMapping("/certificates")
public class CertificateController {

	@Autowired
	private CertificateService certificateService;
	
	
	@PostMapping("/selfsigned")
	public MessageResponseDTO createSelfSignedCertificate(@RequestBody SubjectDTO input){
		System.out.println("Dosao u kontrolerrrrrrrrrrrrrrrrrrr");
		certificateService.selfSignedCertificate(input);
		return new MessageResponseDTO("success self signed");
	}
	
	
	@PostMapping("/signed")
	public MessageResponseDTO createSignedCertificate(@RequestBody SubjectIssuerDTO input){
		
		certificateService.signedCertificate(input);
		return new MessageResponseDTO("success signed");
	}
	
	@PostMapping("/usersigned")
	public MessageResponseDTO createUserSignedCertificate(@RequestBody SubjectIssuerDTO input){
		
		certificateService.usersignedCertificate(input);
		return new MessageResponseDTO("success usersigned");
	}
	
	
	@GetMapping("getCertificate/{id}")
	public MessageResponseDTO getCertificateBySerial(@PathVariable String id){
		
		X509Certificate cert = certificateService.getCertificate(id);

		System.out.println(cert);
		return new MessageResponseDTO("Successfully get certificate");
	}
	
	
	
	@PostMapping("revokeCertificate/{id}")
	public MessageResponseDTO revokeCertificateBySerial(@PathVariable String id){
		
		certificateService.revokeCert(id);
		return new MessageResponseDTO("Successfully revoked certificate");
	}
	
	
	@PostMapping("checkStatus/{id}")
	public MessageResponseDTO checkRevocation(@PathVariable String id){
		
		String answer=certificateService.checkRevocation(id);
		return new MessageResponseDTO(answer);
	}
	
	@GetMapping("/getValidCertificates")
	public List<SSCertificate> getValidCerts(){
		
	List<SSCertificate>allvalid=certificateService.getAllValidCertificates();
		return allvalid;
	}
	
	
	
	
	@GetMapping("getValidity/{id}")
	public MessageResponseDTO getValidityBySerial(@PathVariable String id){
		
		certificateService.checkValidationOCSP(id);
		return new MessageResponseDTO("Successfully validation");
	}
	
	
	
}
