package com.group4bezbednost.bezbednost.service;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group4bezbednost.bezbednost.controller.SubjectDTO;
import com.group4bezbednost.bezbednost.data.IssuerData;
import com.group4bezbednost.bezbednost.data.SubjectData;
import com.group4bezbednost.bezbednost.generators.CertificateGenerator;
import com.group4bezbednost.bezbednost.generators.KeyGenerator;
import com.group4bezbednost.bezbednost.generators.SubjectDataGenerator;
import com.group4bezbednost.bezbednost.keystores.KeyStoreReader;
import com.group4bezbednost.bezbednost.keystores.KeyStoreWriter;

import com.group4bezbednost.bezbednost.model.SSCertificate;
import com.group4bezbednost.bezbednost.repository.CertificateRepository;



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
		
		System.out.println(selfsignedcertificate);
		return selfsignedcertificate;
		
		
	}
	
	
	public void saveCertificate(SSCertificate cert){
		
		certificateRepository.save(cert);
	}
	
	
	
	
	
	
}
