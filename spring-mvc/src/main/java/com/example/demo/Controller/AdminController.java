package com.example.demo.Controller;

import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.Student;
import com.example.demo.ServiceImp.SchoolServiceImpl;
import com.example.demo.ServiceImp.StudentServiceImpl;
import com.example.demo.Util.LogMsg;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private StudentServiceImpl studentService;
	@Autowired
	private SchoolServiceImpl schoolService;

	private static final Logger logger = Logger.getLogger(AdminController.class);

	@RequestMapping("/allStudents")
	@ResponseBody
	ResponseEntity getAllStudents(@RequestParam(defaultValue = "0") int page,
	                              @RequestParam(defaultValue = "5") int size){
		logger.info("admin/allStudents  : initiated");
		ResponseModel responseModel = new ResponseModel(
				200,
				"student list showing page " + (page+1),
				studentService.findAll(page, size));
		logger.info("admin/allStudents  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/deleteStudent/{id}", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity deleteStudentById(@PathVariable("id") int id) {
		logger.info("admin/deleteStudent/{" + id + "}  : initiated");
		ResponseModel responseModel = new ResponseModel(
				200,
				"Delete by id",
				studentService.deleteById(id));
		logger.info("admin/deleteStudent/{" + id + "}  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/getStudent/{id}", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity getStudentById(@PathVariable("id") int id) {
		logger.info("admin/getStudent/{" + id + "}  : initiated");
		ResponseModel responseModel = new ResponseModel(
				200,
				"get by id",
				studentService.getStudent(id));
		logger.info("admin/getStudent/{" + id + "}  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/parentList/{id}", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity getParentList(@PathVariable("id") int id){
		logger.info("admin/parentList/{" + id + "}  : initiated");
		ResponseModel responseModel = new ResponseModel(
				200,
				"get parent list of student",
				studentService.getParentList(id));
		logger.info("admin/parentList/{" + id + "}  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/findAllStudentsSortedBy", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity findAllSortedBy(@RequestParam String sortBy, @RequestParam String direction,
	                               @RequestParam(defaultValue = "0") int page,
	                               @RequestParam(defaultValue = "5") int size){
		logger.info("admin/findAllStudentsSortedBy  : initiated");
		ResponseModel responseModel = new ResponseModel(
				200,
				"find All Students Sorted By " + sortBy + " " + direction + " | showing page " +
						(page+1),
				studentService.getAllStudentsSorted(sortBy, direction, page, size));
		logger.info("admin/findAllStudentsSortedBy  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/StudentSearchBy", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity StudentSearchBy(@RequestParam Optional<String> firstName,
	                        @RequestParam Optional<String> lastName,
	                        @RequestParam Optional<String> phoneNumber,
	                        @RequestParam Optional<String> userName,
	                        @RequestParam Optional<String> address,
	                        @RequestParam Optional<Float> gpa,
	                        @RequestParam Optional<String> parentFirstName,
	                        @RequestParam Optional<String> parentLastName,
	                        @RequestParam(defaultValue = "0") int page,
	                        @RequestParam(defaultValue = "5") int size){
		logger.info("admin/StudentSearchBy  : initiated");
		ResponseModel responseModel = studentService.searchBy(firstName, lastName, phoneNumber,
				address, userName, gpa, parentFirstName, parentLastName, page, size);
		logger.info("admin/StudentSearchBy  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/SchoolSearchBy", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity SchoolSearchBy(@RequestParam Optional<Long> ranking,
	                                             @RequestParam Optional<Integer> numberOfFacultyMembers,
	                                             @RequestParam(defaultValue = "0") int page,
	                                             @RequestParam(defaultValue = "5") int size){
		logger.info("admin/SchoolSearchBy  : initiated");
		ResponseModel responseModel = schoolService.searchBy(ranking, numberOfFacultyMembers, page, size);
		logger.info("admin/SchoolSearchBy  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/addCourse", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity addCourseToStudent(@RequestParam int student_id,
	                                  @RequestParam int course_id){
		logger.info("admin/addCourse  : initiated");
		ResponseModel responseModel = studentService.addCourseToStudentById(student_id, course_id);

		logger.info("admin/addCourse  status:" + responseModel.getStatus());
		if(responseModel.getStatus() == 404){
			return ResponseEntity.status(404).body(responseModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping("/allSchools")
	public @ResponseBody ResponseEntity getAllSchools(@RequestParam(defaultValue = "0") int page,
	                                                  @RequestParam(defaultValue = "5") int size){
		logger.info("admin/allSchools  : initiated");
		ResponseModel responseModel = new ResponseModel(
				200,
				"School list showing page " + (page+1),
				schoolService.findAll(page, size));
		logger.info("admin/allSchools  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/deleteSchool/{id}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity deleteSchoolById(@PathVariable("id") int id) {
		logger.info("admin/deleteSchool/{" + id + "}  : initiated");
		ResponseModel responseModel = new ResponseModel(
				200,
				"Delete by id",
				schoolService.deleteById(id));
		logger.info("admin/deleteSchool/{" + id + "}  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
}
