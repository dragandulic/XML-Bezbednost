package com.group4bezbednost.bezbednost.repository;

import java.math.BigInteger;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4bezbednost.bezbednost.model.SSCertificate;




@Repository
public interface CertificateRepository extends JpaRepository<SSCertificate,Long> {

	
	SSCertificate findOneById(Long id);
	List<SSCertificate>findAll();
	SSCertificate findOneBySerialnum(BigInteger ser);
}
