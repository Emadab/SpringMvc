package com.example.demo.Model;

import javax.persistence.*;
import com.example.demo.Model.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "school")
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	public int getId() {
		return id;
	}

	@Column(name = "number_of_students")
	private int numberOfStudents;
	@Column(name = "number_of_faculty_members")
	private int numberOfFacultyMembers;
	@Column(name = "ranking")
	private long ranking;
	@Column(name = "address")
	private String address;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "password")
	private String password;

	@OneToMany(mappedBy = "school")
	private List<Student> studentList = new ArrayList<Student>();

	public School(){
		this.numberOfStudents=-1;
		this.numberOfFacultyMembers=-1;
		this.ranking=-1;
		this.address ="";
		this.phoneNumber="";
	}

	public School(int numberOfStudents, int numberOfFacultyMembers, long ranking, String address, String phoneNumber){
		this.numberOfStudents=numberOfStudents;
		this.numberOfFacultyMembers=numberOfFacultyMembers;
		this.ranking=ranking;
		this.address =address;
		this.phoneNumber=phoneNumber;
	}

	@Override
	public String toString() {
		return String.format("School[id=%d, numberOfStudents='%s', numberOfFacultyMembers='%s', ranking=%d, address=%s," +
						" phoneNumber=%s]",
				id, numberOfStudents, numberOfFacultyMembers, address, phoneNumber);
	}

	public int getNumberOfStudents() {
		return numberOfStudents;
	}

	public void setNumberOfStudents(int numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	public int getNumberOfFacultyMembers() {
		return numberOfFacultyMembers;
	}

	public void setNumberOfFacultyMembers(int numberOfFacultyMembers) {
		this.numberOfFacultyMembers = numberOfFacultyMembers;
	}

	public long getRanking() {
		return ranking;
	}

	public void setRanking(long ranking) {
		this.ranking = ranking;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@JsonIgnore
	public List<Student> getStudentList() {
		return studentList;
	}

	public School setStudentList(List<Student> studentList) {
		this.studentList = studentList;
		return this;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonSetter
	public void setPassword(String password) {
		this.password = password;
	}
}
