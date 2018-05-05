package com.group4bezbednost.bezbednost.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SSCertificate implements Serializable{

	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String issueralias;
	
	private boolean certificateCA;
	
	private String issuerpass;
	
	private BigInteger serialnum;
	
	private boolean isRevoked;
	
	private String subjectalias;
	
	private String subjpassword;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public boolean isCertificateCA() {
		return certificateCA;
	}

	public void setCertificateCA(boolean certificateCA) {
		this.certificateCA = certificateCA;
	}

	

	public BigInteger getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(BigInteger serialnum) {
		this.serialnum = serialnum;
	}

	public boolean isRevoked() {
		return isRevoked;
	}

	public void setRevoked(boolean isRevoked) {
		this.isRevoked = isRevoked;
	}

	public String getIssuerpass() {
		return issuerpass;
	}

	public void setIssuerpass(String issuerpass) {
		this.issuerpass = issuerpass;
	}

	public String getSubjectalias() {
		return subjectalias;
	}

	public void setSubjectalias(String subjectalias) {
		this.subjectalias = subjectalias;
	}

	public String getSubjpassword() {
		return subjpassword;
	}

	public void setSubjpassword(String subjpassword) {
		this.subjpassword = subjpassword;
	}

	public String getIssueralias() {
		return issueralias;
	}

	public void setIssueralias(String issueralias) {
		this.issueralias = issueralias;
	}

	

	
	
	
	
		
	
	
	
	
	
}
