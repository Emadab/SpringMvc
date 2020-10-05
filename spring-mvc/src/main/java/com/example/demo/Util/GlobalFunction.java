package com.example.demo.Util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Random;

public class GlobalFunction {
	public static String stringToMd5(String s){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte[] digest = md.digest();
			return DatatypeConverter.printHexBinary(digest).toUpperCase();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static int generateRandomCode(){
		Random rng = new Random();
		return rng.nextInt(900000) + 100000;
	}

	public static String getUserNameFromAuth(){
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetails userDetail = (UserDetails) authentication.getPrincipal();
		return userDetail.getUsername();
	}
}
