package com.group4bezbednost.bezbednost.generators;

import java.security.KeyPair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Component;

import com.group4bezbednost.bezbednost.controller.SubjectDTO;
import com.group4bezbednost.bezbednost.data.SubjectData;


@Component
public class SubjectDataGenerator {

	
	
	public SubjectData generateSubjectData(SubjectDTO input) {
		try {
			KeyGenerator kg=new KeyGenerator();
			KeyPair keyPairSubject = kg.generateKeyPair();
			
			//Datumi od kad do kad vazi sertifikat
			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse(input.getStartDate());
			Date endDate = iso8601Formater.parse(input.getEndDate());
			
			//Serijski broj sertifikata
			String sn=input.getSerialnumber();
			//klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		    builder.addRDN(BCStyle.NAME, input.getName());
		    builder.addRDN(BCStyle.SURNAME,input.getSurname());
		    builder.addRDN(BCStyle.O,input.getOrganization());
		    builder.addRDN(BCStyle.C, input.getState());
		    builder.addRDN(BCStyle.E,input.getEmail());
		    //builder.addRDN(BCStyle.SN,input.getSerialnumber());
		    //UID (USER ID) je ID korisnika
		    //builder.addRDN(BCStyle.UID, input.getPassword());
		    
		    //Kreiraju se podaci za sertifikat, sto ukljucuje:
		    // - javni kljuc koji se vezuje za sertifikat
		    // - podatke o vlasniku
		    // - serijski broj sertifikata
		    // - od kada do kada vazi sertifikat
		    return new SubjectData(null, builder.build(), sn, startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
}
