package com.example.demo.Controller;

import com.example.demo.Model.Student;
import com.example.demo.ServiceImp.SchoolServiceImpl;
import com.example.demo.Util.Msg;
import com.example.demo.Model.Course;
import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/school")
public class SchoolController {
	@Autowired
	private SchoolServiceImpl schoolService;

	@RequestMapping("/admin/all")
	public @ResponseBody ResponseEntity getAllSchools(@RequestParam(defaultValue = "0") int page,
	                                                  @RequestParam(defaultValue = "5") int size){
		ResponseModel responseModel = new ResponseModel(
				200,
				"School list showing page " + (page+1),
				schoolService.findAll(page, size));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/admin/delete/{id}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity deleteById(@PathVariable("id") int id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"Delete by id",
				schoolService.deleteById(id));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/user/addStudent", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity addStudent(@RequestParam int school_id, @RequestParam int student_id){
		ResponseModel responseModel = schoolService.addStudentById(school_id, student_id);
		if (responseModel.getStatus() == 404){
			return ResponseEntity.status(404).body(responseModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	//getStudentsOfSchool
	@RequestMapping(value="/user/studentList/{id}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity getStudentList(@PathVariable("id") int id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"get student List of school",
				schoolService.getStudentsOfSchool(id));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/user/findAllSortedBy", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity findAllSortedBy(@RequestParam String sortBy, @RequestParam String direction,
	                                                    @RequestParam(defaultValue = "0") int page,
	                                                    @RequestParam(defaultValue = "5") int size){
		ResponseModel responseModel = new ResponseModel(
				200,
				"find All Students Sorted By " + sortBy + " " + direction + "showing page " + (page+1),
				schoolService.getAllSchoolsSorted(sortBy, direction, page, size));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/user/searchBy", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity searchBy(@RequestParam Optional<Long> ranking,
	                                             @RequestParam Optional<Integer> numberOfFacultyMembers,
	                                             @RequestParam(defaultValue = "0") int page,
	                                             @RequestParam(defaultValue = "5") int size){
		ResponseModel responseModel = schoolService.searchBy(ranking, numberOfFacultyMembers, page, size);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/user/newCourse", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity newCourse(@RequestBody Course course){
		ResponseModel responseModel = schoolService.newCourse(course);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/logIn", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity singIn(@RequestParam String userName,
	                      @RequestParam String password){
		ResponseModel responseModel = schoolService.signIn(userName, password);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/signUp", method= RequestMethod.POST)
	@ResponseBody
	ResponseEntity addNewStudent (@RequestBody School school){
		ResponseModel responseModel = schoolService.addNewSchool(school);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/user/students")
	@ResponseBody
	ResponseEntity getStudents(){
		ResponseModel responseModel = schoolService.getStudents();
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
}
