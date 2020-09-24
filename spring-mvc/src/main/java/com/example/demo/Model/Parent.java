package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.example.demo.Model.Student;
import javax.persistence.*;

@Entity
@Table(name = "parent")
public class Parent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name="student_id")
	private Student student;

	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "role")
	private char role;
	//m-mother/f-father/g-guardian
	@Column(name = "phone_number")
	private String phoneNumber;
	//true: needs to be warned //false: no need
	@Column(name = "warn")
	private boolean warn;

	public Parent(){
		this.firstName ="";
		this.lastName="";
		this.role='n';
		this.phoneNumber="";
		this.warn=false;
	}

	public Parent(String firstName, String lastName, char role, String phoneNumber){
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.phoneNumber = phoneNumber;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public char getRole() {
		return role;
	}

	public void setRole(char role) {
		this.role = role;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@JsonIgnore
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@JsonIgnore
	public boolean isWarn() {
		return warn;
	}

	public void setWarn(boolean warn) {
		this.warn = warn;
	}

	public void setId(int id) {
		this.id = id;
	}
}
