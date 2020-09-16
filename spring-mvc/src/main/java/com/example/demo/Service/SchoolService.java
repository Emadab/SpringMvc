package com.example.demo.Service;

import com.example.demo.Model.Course;
import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.School;
import com.example.demo.Model.Student;

import java.util.List;
import java.util.Optional;

public interface SchoolService {
	String addNewSchool (int numberOfStudents, int numberOfFacultyMembers, long ranking, String address,
	                     String phoneNumber);
	Iterable<School> findAll(int page, int size);

	String deleteById (int id);

	ResponseModel addStudentById(int school_id, int student_id);

	List<Student> getStudentsOfSchool(int school_id);

	List<School> getAllSchoolsSorted(String sortBy, String direction, int page, int size);

	ResponseModel searchBy(Optional<Long> ranking, Optional<Integer> numberOfFacultyMembers,
	                      int page, int size);

	ResponseModel newCourse(Course course);
}
