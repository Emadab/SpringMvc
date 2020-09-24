package com.example.demo.Model;

import javax.persistence.*;
import java.util.Set;

import com.example.demo.Model.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@ManyToMany(mappedBy = "courses")
	Set<Student> students;

	@Column(name = "name")
	private String name;
	@Column(name = "unit")
	private int unit;
	@Column(name = "teacher")
	private String teacher;

	public Course(){
		name = "";
		teacher = "";
		unit = -1;
	}

	public Course(String name, String teacher, int unit){
		this.name = name;
		this.teacher = teacher;
		this.unit = unit;
	}

	public void addStudent(Student student){
		students.add(student);
	}

	@JsonIgnore
	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
}
