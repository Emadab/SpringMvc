package com.example.demo.Util;

public class LogMsg {

	public static String fieldEmpty(String field){
		return field + " field empty";
	}

	public static String deleteFromDb(String object){
		return object + " deleted from database";
	}

	public static String addToDb(String object){
		return object + " added to database";
	}

	public static String retrieveFromDb(String object){
		return object + " retrieved from database";
	}

	public static String notFoundInDb(String object){
		return object + " not found in database";
	}

	public static String authReqSent = "authorization request sent";

	public static String failedLogin = "failed login attempt";

	public static String smsCodeReqSent = "sms code api request sent";
}
