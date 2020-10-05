package com.example.demo.Controller;

import com.example.demo.ServiceImp.StudentServiceImpl;
import com.example.demo.Util.Msg;
import com.example.demo.Model.Parent;
import com.example.demo.Model.PhoneValidationCode.PhoneValidationCode;
import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.Student;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
public class StudentController {

	Logger logger = Logger.getLogger(StudentController.class);

	@Autowired
	private StudentServiceImpl studentService;

	@RequestMapping(value="/addParent", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity addParent(@RequestBody Parent parent,
	                                              @RequestParam int student_id){
		logger.info("student/addParent  : initiated");
		ResponseModel responseModel = studentService.addParentToStudentById(parent, student_id);
		logger.info("student/addParent  status:" + responseModel.getStatus());
		if (responseModel.getStatus() == 404){
			responseModel.setMessage(Msg.studentNotFound);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/changePassword", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity changePassword (@RequestParam String oldPassword,
	                                                    @RequestParam String newPassword){
		logger.info("student/changePassword  : initiated");
		ResponseModel responseModel = studentService.changePassword(oldPassword, newPassword);
		logger.info("student/changePassword  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/sendCode")
	@ResponseBody
	ResponseEntity sendCode(@RequestParam String phoneNumber){
		logger.info("student/sendCode  : initiated");
		ResponseModel responseModel = studentService.sendMessageCode(phoneNumber);
		logger.info("student/sendCode  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/checkCode", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity checkCode(@RequestBody PhoneValidationCode phoneValidationCode){
		logger.info("student/checkCode  : initiated");
		ResponseModel responseModel = studentService.checkCode(phoneValidationCode);
		logger.info("student/checkCode  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/addImage", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity addImage(@RequestParam("file") MultipartFile file,
	                                             @RequestParam int id){
		logger.info("student/addImage  : initiated");
		ResponseModel responseModel = studentService.addImage(file, id);
		logger.info("student/addImage  status:" + responseModel.getStatus());
		if(responseModel.getStatus() == 404){
			return ResponseEntity.status(404).body(responseModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/downloadFile/{id}", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity downloadFile(@PathVariable int id){
		logger.info("student/downloadFile  : initiated");
		logger.info("student/downloadFile/{" + id + "}  status:" + 200);
		return studentService.downloadFile(id);
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity getProfile(){
		logger.info("student/profile  : initiated");
		ResponseModel responseModel = studentService.getProfile();
		logger.info("student/profile  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
}
