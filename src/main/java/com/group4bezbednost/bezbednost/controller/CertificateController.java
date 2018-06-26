package com.group4bezbednost.bezbednost.controller;


import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
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
import javax.xml.bind.DatatypeConverter;
import java.security.cert.X509Certificate;
import java.io.IOException;
import java.io.StringWriter;
import com.group4bezbednost.bezbednost.model.SSCertificate;
import com.group4bezbednost.bezbednost.service.CertificateDecoder;
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
		
		X509Certificate cert = certificateService.getCertificate(id);
		
		CertificateDecoder decoder=new CertificateDecoder();
		
		RSAPublicKey pk=(RSAPublicKey) cert.getPublicKey();
		String decoded=decoder.printCertInfo(cert,pk);
	
		HashMap<String, String> response = new HashMap<>(); 
		response.put("certificate",decoded );
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	
	
	@GetMapping("revokeCertificate/{id}")
	public MessageResponseDTO revokeCertificateBySerial(@PathVariable String id) throws ClassNotFoundException, IOException{
		
		String poruka =certificateService.revokeCert(id);
		if(poruka.equals("Certificate already revoked")) {
			return new MessageResponseDTO("Certificate already revoked");
		}else if(poruka.equals("This certificate doesnt exist")){
			return new MessageResponseDTO("This certificate doesnt exist");
			
		}
		
		return new MessageResponseDTO("Successfully revoked certificate");
	}
	
	
	@GetMapping("checkStatus/{serialnumber}")
	public MessageResponseDTO checkRevocation(@PathVariable String serialnumber) throws ClassNotFoundException, IOException{
		
		String answer=certificateService.checkRevocation(serialnumber);
		return new MessageResponseDTO(answer);
	}
	
	@GetMapping("/getValidIssuers")
	public IssuerResponse getValidIssuers() throws ClassNotFoundException{
		List<String>iss=new ArrayList<String>();
		IssuerResponse ir=new IssuerResponse(iss);
	
		System.out.println("USAO U CONTROLLER GET VALID ISSUERS");
		List<X509Certificate> allvalid = certificateService.getValidCertificates();
		System.out.println("PROSAO METODU GET VALID CERTIFICATES");
		if(allvalid.isEmpty()) {
			System.out.println("vraca null");
			ir.getIssuers().add("no valid issuers");
			return ir;
		}
		System.out.println(allvalid.get(0).getIssuerDN().getName());
		
		for(int i=0;i<allvalid.size();i++){
			
			
			ir.getIssuers().add(allvalid.get(i).getIssuerDN().getName());
		}
		
		if(ir.getIssuers().isEmpty()){
			System.out.println("jeste empty");
			
			ir.getIssuers().add("no issuers");
		}
		
		return ir;
	}
	
	
	public static String certToString(X509Certificate cert) {
	    StringWriter sw = new StringWriter();
	    try {
	        sw.write("-----BEGIN CERTIFICATE-----\n");
	        sw.write(DatatypeConverter.printBase64Binary(cert.getEncoded()).replaceAll("(.{64})", "$1\n"));
	        sw.write("\n-----END CERTIFICATE-----\n");
	    } catch (CertificateEncodingException e) {
	        e.printStackTrace();
	    }
	    return sw.toString();
	}
	
}
