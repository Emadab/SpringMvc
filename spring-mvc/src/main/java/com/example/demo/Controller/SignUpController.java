package com.example.demo.Controller;

import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.School;
import com.example.demo.Model.Student;
import com.example.demo.ServiceImp.SchoolServiceImpl;
import com.example.demo.ServiceImp.StudentServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/signup")
public class SignUpController {

	Logger logger = Logger.getLogger(SignUpController.class);

	@Autowired
	private StudentServiceImpl studentService;
	@Autowired
	private SchoolServiceImpl schoolService;

	@RequestMapping(value="/Student", method= RequestMethod.POST)
	@ResponseBody
	ResponseEntity addNewStudent (@RequestBody Student student,
	                              @RequestParam int smsCode){
		logger.info("signup/Student  : initiated");
		ResponseModel responseModel = studentService.addNewStudent(student, smsCode);
		logger.info("signup/Student  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/School", method= RequestMethod.POST)
	@ResponseBody
	ResponseEntity addNewStudent (@RequestBody School school){
		logger.info("signup/School  : initiated");
		ResponseModel responseModel = schoolService.addNewSchool(school);
		logger.info("signup/School  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
}
