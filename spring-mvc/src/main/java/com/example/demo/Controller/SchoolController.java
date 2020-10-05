package com.example.demo.Controller;

import com.example.demo.Model.Student;
import com.example.demo.ServiceImp.SchoolServiceImpl;
import com.example.demo.Util.Msg;
import com.example.demo.Model.Course;
import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.School;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/school")
public class SchoolController {

	Logger logger = Logger.getLogger(SchoolController.class);

	@Autowired
	private SchoolServiceImpl schoolService;

	@RequestMapping(value="/addStudent", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity addStudent(@RequestParam int school_id, @RequestParam int student_id){
		logger.info("school/addStudent  : initiated");
		ResponseModel responseModel = schoolService.addStudentById(school_id, student_id);
		logger.info("school/addStudent  status:" + responseModel.getStatus());
		if (responseModel.getStatus() == 404){
			return ResponseEntity.status(404).body(responseModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	//getStudentsOfSchool
	@RequestMapping(value="/studentList/{id}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity getStudentList(@PathVariable("id") int id) {
		logger.info("school/studentList/{" + id + "}  : initiated");
		ResponseModel responseModel = new ResponseModel(
				200,
				"get student List of school",
				schoolService.getStudentsOfSchool(id));
		logger.info("school/studentList/{" + id + "}  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/findAllSortedBy", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity findAllSortedBy(@RequestParam String sortBy, @RequestParam String direction,
	                                                    @RequestParam(defaultValue = "0") int page,
	                                                    @RequestParam(defaultValue = "5") int size){
		logger.info("school/findAllSortedBy  : initiated");
		ResponseModel responseModel = new ResponseModel(
				200,
				"find All Students Sorted By " + sortBy + " " + direction + "showing page " + (page+1),
				schoolService.getAllSchoolsSorted(sortBy, direction, page, size));
		logger.info("school/findAllSortedBy  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/newCourse", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity newCourse(@RequestBody Course course){
		logger.info("school/newCourse  : initiated");
		ResponseModel responseModel = schoolService.newCourse(course);
		logger.info("school/newCourse  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/students")
	@ResponseBody
	ResponseEntity getStudents(){
		logger.info("school/students  : initiated");
		ResponseModel responseModel = schoolService.getStudents();
		logger.info("school/students  status:" + responseModel.getStatus());
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
}
