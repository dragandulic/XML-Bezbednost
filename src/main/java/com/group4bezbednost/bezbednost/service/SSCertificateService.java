package com.group4bezbednost.bezbednost.service;

import java.math.BigInteger;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group4bezbednost.bezbednost.model.SSCertificate;
import com.group4bezbednost.bezbednost.repository.CertificateRepository;

@Service
public class SSCertificateService {
	
	
	@Autowired
	private CertificateRepository certificateRepository;
	
	
	public SSCertificate getCerOfId(Long id) {
		return certificateRepository.findOneById(id);
	}
	
	public void saveSSCertificate(SSCertificate ssc){
		
		certificateRepository.save(ssc);
	}
	
	public String getPassByAlias(String alias){
		
		return certificateRepository.findPasswordByAlias(alias);
	}
	
public Long getIdBySerial(String s){
		
		return certificateRepository.findIdByAlias(s);
	}

}
