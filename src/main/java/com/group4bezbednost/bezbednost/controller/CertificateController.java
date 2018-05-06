package com.group4bezbednost.bezbednost.controller;


import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group4bezbednost.bezbednost.model.SSCertificate;
import com.group4bezbednost.bezbednost.service.CertificateService;
import com.group4bezbednost.bezbednost.service.SSCertificateService;


@CrossOrigin(origins="http://localhost:4200",allowedHeaders="*")
@RestController
@RequestMapping("/certificates")
public class CertificateController {

	@Autowired
	private CertificateService certificateService;
	
	
	@Autowired
	private SSCertificateService sscertificateService; 
	
	
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
	
	@RequestMapping(value="/getCertificateOfId/{id}",method=RequestMethod.GET)
	public ResponseEntity<SSCertificate> getCertificateOfId(@PathVariable Long id){
		
		SSCertificate ssc = sscertificateService.getCerOfId(id);
		if(!(ssc==null)) {
			return new ResponseEntity<>(ssc,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	} 
	
	
	@PostMapping("/usersigned")
	public MessageResponseDTO createUserSignedCertificate(@RequestBody SubjectIssuerDTO input){
		
		certificateService.usersignedCertificate(input);
		return new MessageResponseDTO("success usersigned");
	}
	
	
	@GetMapping("getCertificate/{id}")
	public ResponseEntity<?> getCertificateBySerial(@PathVariable String id) throws CertificateEncodingException{
		
		Certificate cert = certificateService.getCertificate(id);
	
		System.out.println(cert);
		HashMap<String, String> response = new HashMap<>(); 
		response.put("certificate", Base64Utils.encodeToString(cert.getEncoded()));
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	
	
	@GetMapping("revokeCertificate/{id}")
	public MessageResponseDTO revokeCertificateBySerial(@PathVariable String id){
		
		String poruka =certificateService.revokeCert(id);
		if(poruka.equals("not successfully")) {
			return new MessageResponseDTO("Unsuccessfully revoked certificate");
		}
		return new MessageResponseDTO("Successfully revoked certificate");
	}
	
	
	@GetMapping("checkStatus/{id}")
	public MessageResponseDTO checkRevocation(@PathVariable String id){
		
		String answer=certificateService.checkRevocation(id);
		return new MessageResponseDTO(answer);
	}
	
	@GetMapping("/getValidCertificates")
	public ResponseEntity<List<SSCertificate>> getValidCerts(){
	
		List<SSCertificate> allvalid = certificateService.getAllValidCertificates();

		if(!(allvalid==null)) {
			return new ResponseEntity<>(allvalid,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	
	
	@GetMapping("getValidity/{id}")
	public MessageResponseDTO getValidityBySerial(@PathVariable String id){
		
		certificateService.checkValidationOCSP(id);
		return new MessageResponseDTO("Successfully validation");
	}
	
	
	
}
