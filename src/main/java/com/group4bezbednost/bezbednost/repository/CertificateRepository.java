package com.group4bezbednost.bezbednost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group4bezbednost.bezbednost.model.SSCertificate;





public interface CertificateRepository extends JpaRepository<SSCertificate,Long> {

	
	SSCertificate findOneById(Long id);
}
