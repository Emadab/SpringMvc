package com.example.demo.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
@Table(name = "student")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "address")
	private String address;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(name = "gpa")
	private float gpa;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "password")
	private String password;
	@Column(name = "last_log_in")
	private Timestamp lastLogIn;


	@ManyToOne
	@JoinColumn(name = "school_id")
	private School school;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Parent> parentList = new ArrayList<Parent>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "img_id")
	private StudentImage studentImage;

	@ManyToMany
	@JoinTable(
		name = "course_students",
		joinColumns = @JoinColumn(name = "students_id"),
		inverseJoinColumns = @JoinColumn(name = "courses_id"))
	Set<Course> courses;

	public Student(){
		this.firstName="";
		this.lastName="";
		this.address="";
		this.phoneNumber="";
		this.gpa=-1;
		this.userName="";
		this.password="";
	}

	public Student(String firstName, String lastName, String address, String phoneNumber, float gpa,
	               String userName,String password, StudentImage studentImage){
		this.firstName=firstName;
		this.lastName=lastName;
		this.address=address;
		this.phoneNumber=phoneNumber;
		this.gpa=gpa;
		this.userName=userName;
		this.password = password;
		this.studentImage = studentImage;
	}

	@Override
	public String toString() {
		return String.format("Student[id=%d, firstName='%s', lastName='%s', address=%s, phoneNumber=%s, gpa='%f'," +
						" username=%s]",
				id, firstName, lastName, address, phoneNumber, gpa, userName);
	}

	public void addCourse(Course course){
		courses.add(course);
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	@JsonIgnore
	public Set<Course> getCourses() {
		return courses;
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

	public float getGpa() {
		return gpa;
	}

	public void setGpa(float gpa) {
		this.gpa = gpa;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<Parent> getParentList() {
		return parentList;
	}

	public Student setParentList(List<Parent> parentList) {
		this.parentList = parentList;
		return this;
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

	public StudentImage getStudentImage() {
		return studentImage;
	}

	public void setStudentImage(StudentImage studentImage) {
		this.studentImage = studentImage;
	}

	@JsonIgnore
	public Timestamp getLastLogIn() {
		return lastLogIn;
	}

	public void setLastLogIn(Timestamp lastLogIn) {
		this.lastLogIn = lastLogIn;
	}

	public void setId(int id) {
		this.id = id;
	}
}
