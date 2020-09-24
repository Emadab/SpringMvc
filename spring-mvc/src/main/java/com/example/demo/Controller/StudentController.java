package com.example.demo.Controller;

import com.example.demo.ServiceImp.StudentServiceImpl;
import com.example.demo.Util.Msg;
import com.example.demo.Model.Parent;
import com.example.demo.Model.PhoneValidationCode.PhoneValidationCode;
import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private StudentServiceImpl studentService;

	@RequestMapping(value="/add", method= RequestMethod.POST)
	@ResponseBody
	ResponseEntity addNewStudent (@RequestBody Student student,
	                                                   @RequestParam int smsCode){
		ResponseModel responseModel = studentService.addNewStudent(student, smsCode);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/adds", method= RequestMethod.POST)
	@ResponseBody Object addsNewStudent (@RequestBody Student student){
		return student;
	}

	@RequestMapping("/all")
	@ResponseBody
	ResponseEntity getAllStudents(@RequestParam(defaultValue = "0") int page,
	                                                   @RequestParam(defaultValue = "5") int size){
		ResponseModel responseModel = new ResponseModel(
				200,
				"student list showing page " + (page+1),
				studentService.findAll(page, size));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity deleteById(@PathVariable("id") int id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"Delete by id",
				 studentService.deleteById(id));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity getById(@PathVariable("id") int id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"get by id",
				studentService.getStudent(id));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/addParent", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity addParent(@RequestBody Parent parent,
	                                              @RequestParam int student_id){
		ResponseModel responseModel = studentService.addParentToStudentById(parent, student_id);
		if (responseModel.getStatus() == 404){
			responseModel.setStatus(404);
			responseModel.setMessage(Msg.studentNotFound);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/parentList/{id}", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity getParentList(@PathVariable int id){
		ResponseModel responseModel = new ResponseModel(
				200,
				"get parent list of student",
				studentService.getParentList(id));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/findAllSortedBy", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity findAllSortedBy(@RequestParam String sortBy, @RequestParam String direction,
	                                                    @RequestParam(defaultValue = "0") int page,
	                                                    @RequestParam(defaultValue = "5") int size){
		ResponseModel responseModel = new ResponseModel(
				200,
				"find All Students Sorted By " + sortBy + " " + direction + " | showing page " +
						(page+1),
				studentService.getAllStudentsSorted(sortBy, direction, page, size));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/searchBy", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity searchBy(@RequestParam Optional<String> firstName,
	                                             @RequestParam Optional<String> lastName,
	                                             @RequestParam Optional<String> phoneNumber,
	                                             @RequestParam Optional<String> userName,
	                                             @RequestParam Optional<String> address,
	                                             @RequestParam Optional<Float> gpa,
	                                             @RequestParam Optional<String> parentFirstName,
	                                             @RequestParam Optional<String> parentLastName,
	                                             @RequestParam(defaultValue = "0") int page,
	                                             @RequestParam(defaultValue = "5") int size){
			ResponseModel responseModel = studentService.searchBy(firstName, lastName, phoneNumber,
					address, userName, gpa, parentFirstName, parentLastName, page, size);
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/changePassword", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity changePassword (@RequestParam int id,
	                                                    @RequestParam String oldPassword,
	                                                    @RequestParam String newPassword){
		ResponseModel responseModel = studentService.changePassword(id, oldPassword, newPassword);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/logIn", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity singIn(@RequestParam String userName,
	                                           @RequestParam String password){
		ResponseModel responseModel = studentService.signIn(userName, password);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/sendCode")
	@ResponseBody
	ResponseEntity sendCode(@RequestParam String phoneNumber){
		ResponseModel responseModel = studentService.sendMessageCode(phoneNumber);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/checkCode", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity checkCode(@RequestBody PhoneValidationCode phoneValidationCode){
		ResponseModel responseModel = studentService.checkCode(phoneValidationCode);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/addImage", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity addImage(@RequestParam("file") MultipartFile file,
	                                             @RequestParam int id){
		ResponseModel responseModel = studentService.addImage(file, id);
		if(responseModel.getStatus() == 404){
			return ResponseEntity.status(404).body(responseModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/downloadFile/{id}", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity downloadFile(@PathVariable int id){
		return studentService.downloadFile(id);
	}

	@RequestMapping(value = "/addCourse", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity addCourseToStudent(@RequestParam int student_id,
	                                                       @RequestParam int course_id){
		ResponseModel responseModel = studentService.addCourseToStudentById(student_id, course_id);

		if(responseModel.getStatus() == 404){
			return ResponseEntity.status(404).body(responseModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity getProfile(){
		ResponseModel responseModel = studentService.getProfile();
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
}
