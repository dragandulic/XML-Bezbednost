package com.group4bezbednost.bezbednost.service;

import java.io.IOException;
import java.io.StringReader;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class CertificateDecoder {

      public CertificateDecoder(){
    	  
      }
	
      

      
  
    public String printCertInfo(X509Certificate cert, RSAPublicKey rsaPublicKey) {
    	
    	
    	  rsaPublicKey = (RSAPublicKey)cert.getPublicKey();
        StringBuilder sb = new StringBuilder();

        sb.append("Certificate:\r\n");
        sb.append("Data:\r\n");
        sb.append("\tVersion: " + cert.getVersion() + "\r\n");
        sb.append("\tSerial Number: " + cert.getSerialNumber() + "\r\n");
        sb.append("\tSignature Algorithm: " + cert.getSigAlgName() + "\r\n");
        sb.append("\tIssuer: " + cert.getIssuerDN().getName() + "\r\n");
        sb.append("\tSignature Algorithm: " + cert.getSigAlgName() + "\r\n");
        sb.append("\tValidity\r\n");
        sb.append("\t\tNot Before: " + cert.getNotBefore() + "\r\n");
        sb.append("\t\tNot After: " + cert.getNotAfter() + "\r\n");
        sb.append("\tSubject: " + cert.getSubjectDN() + "\r\n");
        sb.append("\tSubject Public Key Info:\r\n");
        sb.append("\t\tPublic Key Algorithm: " + rsaPublicKey.getAlgorithm() + "\r\n");
        sb.append("\t\t\tPublic-Key: " + rsaPublicKey.getModulus().bitLength() + " bit \r\n");
     
        // TODO: Print other attributes, do some research to get them all...

        return sb.toString();
    }

}