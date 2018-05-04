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
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;

import java.math.BigInteger;

import org.bouncycastle.asn1.x500.X500Name;
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
	private SSCertificateService ssCertificateService;
	
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
		
       
       
		X509Certificate selfsignedcertificate =  certificateGenerator.generateCertificate(subjectData, issuer);
		keyStoreWriter.write(opt, keyPairSubject.getPrivate(), pass.toCharArray(), selfsignedcertificate);
		
		SSCertificate ssc=new SSCertificate();
		ssc.setAlias(opt);
		ssc.setCertificateCA(subjectDTO.isCA());
		ssc.setSerialnum(selfsignedcertificate.getSerialNumber());
		ssc.setRevoked(false);
		ssCertificateService.saveSSCertificate(ssc);
		
		
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
		
		List<SSCertificate>listc=ssCertificateService.findAllCertificates();
		for(SSCertificate sssc:listc){
			
			if(!(sssc.getAlias().equals(issueralias) && sssc.isCertificateCA()==true)){
				
				System.out.println("Only CA certificates can sign other certificates");
				return null;
			}
		}
				
				IssuerData issuer = keyStoreReader.readIssuerFromStore("keystore", 
						issueralias, 
						keyStorePassword.toCharArray(), 
						issuerpass.toCharArray());
				
						
				
			
				System.out.println(subjectData.getSerialNumber());
				
				
				X509Certificate signedcertificate =  certificateGenerator.generateCertificate(subjectData, issuer);
				keyStoreWriter.write(opt, keyPairSubject.getPrivate(), pass.toCharArray(), signedcertificate);
				
				SSCertificate ssc=new SSCertificate();
				ssc.setAlias(opt);
				ssc.setCertificateCA(subjectissuerDTO.isCA());
				ssc.setRevoked(false);
				ssc.setSerialnum(signedcertificate.getSerialNumber());
				ssCertificateService.saveSSCertificate(ssc);
				
				System.out.println(signedcertificate);
				
				return signedcertificate;
				
			
			
			
			
		
		
		
				
		
	}
	
	public void checkValidationOCSP(String serialnumber){
		//online certificate status protocol : sluzi da proverimo stanje u kom se nalazi sertifikat, da li je istekao itd...tj da li je validan
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
				        
				        
				        if(serialkeystore.equals(serialnumber)){
				       
				        	
				                try {
				                	 
				                    certificate.checkValidity();
				                    System.out.println("Certificate is active for current date");
				                } catch(CertificateExpiredException cee) {
				                    System.out.println("Certificate is expired");
				                }
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
				       
				        
				        if(serialkeystore.equals(serialnumber)){
				       
				        
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
	
	public void revokeCert(String serialnum){
		
		BigInteger serial=new BigInteger(serialnum);
		SSCertificate c=ssCertificateService.findBySerialn(serial);
		
		c.setRevoked(true);
		ssCertificateService.saveSSCertificate(c);
		
	}
	
}
