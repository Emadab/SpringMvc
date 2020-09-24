package com.example.demo.Service;

import com.example.demo.Model.Parent;
import com.example.demo.Model.PhoneValidationCode.PhoneValidationCode;
import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StudentService {
	ResponseModel addNewStudent (Student student, int validationCode);

	List<Student> findAll(int page, int size);

	String deleteById (int id);

	Student getStudent(int id);

	ResponseModel addToSchoolById(int school_id, int student_id);

	ResponseModel addParentToStudentById(Parent parent, int student_id);

	List<Parent> getParentList(int student_id);

	List<Student> getAllStudentsSorted(String sortBy, String direction, int page, int size);

	ResponseModel searchBy(Optional<String> firstName, Optional<String> lastName,
	                       Optional<String> phoneNumber, Optional<String> address, Optional<String> userName,
	                       Optional<Float> gpa, Optional<String> parentFirstName,
	                       Optional<String> parentLastName, int page, int size);

	ResponseModel changePassword(int student_id, String oldPassword, String newPassword);

	ResponseModel signIn(String userName, String password);

	ResponseModel sendMessageCode(String phoneNumber);

	ResponseModel checkCode(PhoneValidationCode phoneValidationCode);

	ResponseModel addImage(MultipartFile file, int id);

	ResponseEntity downloadFile(int id);

	ResponseModel addCourseToStudentById(int student_id, int course_id);

	void checkStudentActivity();

	ResponseModel getProfile();
}
