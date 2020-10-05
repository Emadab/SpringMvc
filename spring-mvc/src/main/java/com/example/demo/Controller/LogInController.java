package com.example.demo.Controller;

import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.ServiceImp.SchoolServiceImpl;
import com.example.demo.ServiceImp.StudentServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LogInController {

	private Logger logger = Logger.getLogger(LogInController.class);

	@Autowired
	private StudentServiceImpl studentService;
	@Autowired
	private SchoolServiceImpl schoolService;

	@RequestMapping(value="/Student", method= RequestMethod.GET)
	@ResponseBody
	ResponseEntity StudentSingIn(@RequestParam String userName,
	                      @RequestParam String password){
		logger.info("login/Student  : initiated");
		ResponseModel responseModel = studentService.signIn(userName, password);
		logger.info("login/Student  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/School", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity SchoolSingIn(@RequestParam String userName,
	                      @RequestParam String password){
		logger.info("login/School  : initiated");
		ResponseModel responseModel = schoolService.signIn(userName, password);
		logger.info("login/School  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
}
