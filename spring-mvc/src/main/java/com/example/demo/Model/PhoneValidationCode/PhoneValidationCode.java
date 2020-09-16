package com.example.demo.Model.PhoneValidationCode;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import javax.persistence.*;

@Entity
@Table(name = "otp")
public class PhoneValidationCode {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String phoneNumber;
	private int code;

	public PhoneValidationCode(){
		this.phoneNumber = "";
		this.code = -1;
	}

	public PhoneValidationCode(String phoneNumber, int code){
		this.phoneNumber = phoneNumber;
		this.code = code;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
