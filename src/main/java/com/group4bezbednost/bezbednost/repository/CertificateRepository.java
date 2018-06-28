package com.group4bezbednost.bezbednost.repository;

import java.math.BigInteger;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group4bezbednost.bezbednost.model.SSCertificate;





@Repository
public interface CertificateRepository extends JpaRepository<SSCertificate,Long> {

	
	SSCertificate findOneById(Long id);
	
	@Query(value="SELECT DISTINCT c.issuerpass FROM SSCertificate c WHERE  (c.issueralias)=(:alias)")
	String  findPasswordByAlias(@Param("alias")String alias);
	
	
	@Query(value="SELECT  c.id FROM SSCertificate c WHERE  (c.serialnumber)=(:serial)")
	Long findIdByAlias(@Param("serial")String serial);
}
