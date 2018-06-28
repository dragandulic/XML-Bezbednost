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
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	@Value("${crl.file}")
	private String crlFile;
	
	private  ArrayList<X509Certificate>list=new ArrayList<X509Certificate>();
	
	
	
	
	
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
		
		
		//callList(selfsignedcertificate);
		SSCertificate temp=new SSCertificate();
		temp.setIssueralias(opt);
		temp.setIssuerpass(pass);
		temp.setSerialnumber(subjectDTO.getSerialnumber());
		ssCertificateService.saveSSCertificate(temp);
		
		return selfsignedcertificate;
		
		
	}
	
	public X509Certificate signedCertificate(SubjectIssuerDTO subjectissuerDTO,String issueraliasform){
		
		String issueralias=issueraliasform;
		String issuerpass=ssCertificateService.getPassByAlias(issueralias);
		//String issuerpass=subjectissuerDTO.getIssuerpassword();
		System.out.println(issueralias+"bbbbbbbbbbb");
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
				
						
				
			
				
				
				
				X509Certificate signedcertificate =  certificateGenerator.generateCertificate(subjectData, issuer);
				keyStoreWriter.write(opt, keyPairSubject.getPrivate(), pass.toCharArray(), signedcertificate);
				
				//callList(signedcertificate);
				SSCertificate temp=new SSCertificate();
				temp.setIssueralias(issueralias);
				temp.setIssuerpass(issuerpass);
				temp.setSerialnumber(subjectissuerDTO.getSerialnumber());
				ssCertificateService.saveSSCertificate(temp);
				
				
				
				return signedcertificate;
				
			
				
		
	}
	
public X509Certificate usersignedCertificate(SubjectIssuerDTO subjectissuerDTO){
		/*
		String issueralias=subjectissuerDTO.getIssueralias();
		String issuerpass=subjectissuerDTO.getIssuerpassword();
		System.out.println(issueralias+"bbbbbbbbbbb");
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
				
				
				return signedcertificate;
				
			
				
*/		
	return null;
	}







 
	
	public String checkExpiration(String serialnumber) throws CertificateNotYetValidException {
		//online certificate status protocol : sluzi da proverimo stanje u kom se nalazi sertifikat, da li je istekao itd...tj da li je validan
		KeyStore keyStore;
	
		X509Certificate certificate=getCertificate(serialnumber);
		
				        	
				                try {
								certificate.checkValidity();
								
								
								return "nonexpired";
								
								}catch(CertificateExpiredException cee) {
								    return "expired";
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
	
public ArrayList<X509Certificate> getValidCertificates() throws ClassNotFoundException {
		
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
				       
				        System.out.println("Upravo proveravam ovaj sertifikat: "+serialkeystore);
				     String check=checkExpiration(serialkeystore);
				     System.out.println("Upravo proveravam ovaj sertifikat: "+serialkeystore);
					 String r=checkRevocation(serialkeystore);
					 System.out.println(r+" da li je povucen ili nije"+" provera za"+serialkeystore);
			    	 Long sert=ssCertificateService.getIdBySerial(serialkeystore);
				     if(check.equals("nonexpired") && r.equals("not revocated")){
				    	 System.out.println(serialkeystore +" je validan!");
				    	 
				    	list.add(certificate); 
				    	 }else{
				    		 
				    		 
				    		 if(list.contains(certificate)){
				    			 list.remove(certificate);
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
		
		
	
		
		
		
		 
		
		return list;
	}
	
	public String revokeCert(String serialnum) throws IOException, ClassNotFoundException{
		
		X509Certificate cert = getCertificate(serialnum);
		if(cert==null){
			return "This certificate doesnt exist";
		}
		
		ArrayList<X509Certificate> listCert = new ArrayList<>();
		
		//FileInputStream fis = new FileInputStream(crlFile);
	//	ObjectInputStream ois = new ObjectInputStream(fis);
	//	listCert = (ArrayList<X509Certificate>) ois.readObject();
		
		//if(listCert!=null){
	//	return "Certificate already revoked";	
		
			
			
			listCert.add(cert);
			FileOutputStream fos = new FileOutputStream(crlFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(listCert);
			oos.close();
		
		
		
		
		return "Successfully revoked certificate";
		}
	
	
	public String checkRevocation(String serialnum) throws IOException, ClassNotFoundException{ // provera da li je sertifikat povucen
		BigInteger serial=new BigInteger(serialnum);
         X509Certificate cer=getCertificate(serialnum); 
         if(cer==null){
        	 
        	return "This certificate is not generated"; 
         }
         
         
         FileInputStream fos = new FileInputStream(crlFile);
         
 		ObjectInputStream oos = new ObjectInputStream(fos);
 		ArrayList<X509Certificate> listCert = (ArrayList<X509Certificate>) oos.readObject();
 		System.out.println(listCert.get(0));
 		oos.close();
 		
 		if(listCert.contains(cer)){
 			return "revocated";
 		}
 		else{
 			return "not revocated";
 		}
 	
 	}
         
     public String getAlias(String serialnum){
    	 
    	 
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
				       
				        
				        if(serialkeystore.equals(serialnum)){
				       
				        
				        return alias;
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

	public ArrayList<X509Certificate> getList() {
		return list;
	}

	public void setList(ArrayList<X509Certificate> list) {
		this.list = list;
	}
         
         
         public void callList(X509Certificate cert){
        	 
        	 list.add(cert);
        	
        	 
         }
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
      
	
}
