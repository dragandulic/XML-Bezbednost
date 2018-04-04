package com.group4bezbednost.bezbednost.service;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;

import javax.annotation.PostConstruct;

import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.group4bezbednost.bezbednost.controller.SubjectDTO;
import com.group4bezbednost.bezbednost.controller.SubjectIssuerDTO;
import com.group4bezbednost.bezbednost.data.IssuerData;
import com.group4bezbednost.bezbednost.data.SubjectData;
import com.group4bezbednost.bezbednost.generators.CertificateGenerator;
import com.group4bezbednost.bezbednost.generators.KeyGenerator;
import com.group4bezbednost.bezbednost.generators.SubjectDataGenerator;
import com.group4bezbednost.bezbednost.keystores.KeyStoreReader;
import com.group4bezbednost.bezbednost.keystores.KeyStoreWriter;

import com.group4bezbednost.bezbednost.model.SSCertificate;
import com.group4bezbednost.bezbednost.repository.CertificateRepository;
import sun.security.provider.certpath.OCSP;
import sun.security.x509.X509CertImpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;



@Service
public class CertificateService {

	@Autowired
	private KeyGenerator keyGenerator;
	@Autowired
	private CertificateGenerator certificateGenerator;
	@Autowired
	private KeyStoreWriter keyStoreWriter;
	@Autowired
	private KeyStoreReader keyStoreReader;
	
	@Autowired
	private CertificateRepository certificateRepository;
	
	@Value("${keyStore.file}")
	private String keyStoreFile;
	
	@Value("${keyStore.password}")
	
	private String keyStorePassword;
	private KeyStore keyStore;
	
	
	
	
	
	public X509Certificate selfSignedCertificate(SubjectDTO subjectDTO){
		
		String opt=subjectDTO.getOptionalCompanyName();
		String pass=subjectDTO.getPassword();
		
		
		SubjectDataGenerator sdg=new SubjectDataGenerator();
		SubjectData subjectData=sdg.generateSubjectData(subjectDTO);
		
		KeyPair keyPairSubject = keyGenerator.generateKeyPair();
		subjectData.setPublicKey(keyPairSubject.getPublic());
		
		
       IssuerData issuer = new IssuerData(keyPairSubject.getPrivate(), subjectData.getX500name());
		
       
       if(subjectDTO.isCA()==false){
    	   
    	   System.out.println("Just certificates with CA==true can be generated");
    	   return null;
       }
       
		X509Certificate selfsignedcertificate =  certificateGenerator.generateCertificate(subjectData, issuer);
		keyStoreWriter.write(opt, keyPairSubject.getPrivate(), pass.toCharArray(), selfsignedcertificate);
		
		System.out.println(selfsignedcertificate);
		return selfsignedcertificate;
		
		
	}
	
	public X509Certificate signedCertificate(SubjectIssuerDTO subjectissuerDTO){
		
		String issueralias=subjectissuerDTO.getIssueralias();
		String issuerpass=subjectissuerDTO.getIssuerpassword();
		
		String opt=subjectissuerDTO.getOptionalCompanyName();
		String pass=subjectissuerDTO.getPassword();
		SubjectDTO sd=new SubjectDTO();
		
		sd.setName(subjectissuerDTO.getName());
		sd.setSurname(subjectissuerDTO.getSurname());
		sd.setOrganization(subjectissuerDTO.getOrganization());
		sd.setState(subjectissuerDTO.getState());
		sd.setEmail(subjectissuerDTO.getEmail());
		sd.setSerialnumber(subjectissuerDTO.getSerialnumber());
		sd.setOptionalCompanyName(subjectissuerDTO.getOptionalCompanyName());
		sd.setStartDate(subjectissuerDTO.getStartDate());
		sd.setEndDate(subjectissuerDTO.getEndDate());
		
		SubjectDataGenerator sdg=new SubjectDataGenerator();
		SubjectData subjectData=sdg.generateSubjectData(sd);
		
		
		KeyPair keyPairSubject = keyGenerator.generateKeyPair();
		subjectData.setPublicKey(keyPairSubject.getPublic());
		
		IssuerData issuer = keyStoreReader.readIssuerFromStore("keystore", 
				issueralias, 
				keyStorePassword.toCharArray(), 
				issuerpass.toCharArray());
		
				
		
		System.out.println(subjectData.getSerialNumber());
		X509Certificate signedcertificate =  certificateGenerator.generateCertificate(subjectData, issuer);
		keyStoreWriter.write(opt, keyPairSubject.getPrivate(), pass.toCharArray(), signedcertificate);
		
		System.out.println(signedcertificate);
		
		return signedcertificate;
				
		
	}
	
	public void checkValidationOCSP(String serialnumber){
		//online certificate status protocol : sluzi da proverimo stanje u kom se nalazi sertifikat, da li je istekao itd...tj da li je validan
		
	
		
		//X509Certificate cert = (X509Certificate) keyStoreReader.readCertificate(keystoreFile, keyStorePassword, serialnumber);
		
	}
	
	public X509Certificate getCertificate(String serialnumber) {
		
		//X509Certificate x=(X509Certificate) keyStoreReader.readCertificate("keystore","root", serialnumber);
	
		 KeyStore keyStore;
		 
		 
		 
		 try {
			 KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			 BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
				ks.load(in, keyStorePassword.toCharArray());
			 
				
				Enumeration enumeration = ks.aliases();
				 while(enumeration.hasMoreElements()) {
				        String alias = (String)enumeration.nextElement();
				        X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
				       
				        BigInteger serial = certificate.getSerialNumber();
				        String serialkeystore=serial.toString();
				        System.out.println(serial+"ovo je iz key stora ");
				        
				        if(serialkeystore.equals(serialnumber)){
				        System.out.println(serial + "ispisan serijski broj"); 
				        return certificate;
				        }
				 }
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	
		
		
		
		 
		
		return null;
	}
	
}
