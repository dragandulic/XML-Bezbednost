package service;

import java.security.KeyPair;
import java.security.cert.Certificate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import controller.SubjectDTO;
import data.IssuerData;
import data.SubjectData;
import generators.CertificateGenerator;
import generators.KeyGenerator;
import generators.SubjectDataGenerator;
import keystores.KeyStoreReader;
import keystores.KeyStoreWriter;

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
	
	
	
	public Certificate selfSignedCertificate(SubjectDTO subjectDTO){
		
		String opt=subjectDTO.getOptionalCompanyName();
		String pass=subjectDTO.getPassword();
		
		
		SubjectDataGenerator sdg=new SubjectDataGenerator();
		SubjectData subjectData=sdg.generateSubjectData(subjectDTO);
		
		KeyPair keyPairSubject = keyGenerator.generateKeyPair();
		subjectData.setPublicKey(keyPairSubject.getPublic());
		
		
       IssuerData issuer = new IssuerData(keyPairSubject.getPrivate(), subjectData.getX500name());
		
		Certificate selfsignedcertificate = certificateGenerator.generateCertificate(subjectData, issuer);
		keyStoreWriter.write(opt, keyPairSubject.getPrivate(), pass.toCharArray(), selfsignedcertificate);
		
		
		return selfsignedcertificate;
		
		
	}
	
	
	
	
	
	
	
	
	
}
