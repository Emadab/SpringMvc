package com.example.demo.Controller;

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

	@RequestMapping(value="/add", method= RequestMethod.POST)
	public @ResponseBody ResponseEntity addNewSchool (@RequestParam int numberOfStudents,
	                                                  @RequestParam int numberOfFacultyMembers,
	                                                  @RequestParam long ranking, @RequestParam String address,
	                                                  @RequestParam String phoneNumber){
		ResponseModel responseModel = new ResponseModel(
				200,
				"added School",
				schoolService.addNewSchool(numberOfStudents, numberOfFacultyMembers, ranking, address, phoneNumber)
		);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping("/all")
	public @ResponseBody ResponseEntity getAllSchools(@RequestParam(defaultValue = "0") int page,
	                                                  @RequestParam(defaultValue = "5") int size){
		ResponseModel responseModel = new ResponseModel(
				200,
				"School list showing page " + (page+1),
				schoolService.findAll(page, size));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity deleteById(@PathVariable("id") int id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"Delete by id",
				schoolService.deleteById(id));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/addStudent", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity addStudent(@RequestParam int school_id, @RequestParam int student_id){
		ResponseModel responseModel = schoolService.addStudentById(school_id, student_id);
		if (responseModel.getStatus() == 404){
			return ResponseEntity.status(404).body(responseModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	//getStudentsOfSchool
	@RequestMapping(value="/studentList/{id}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity getStudentList(@PathVariable("id") int id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"get student List of school",
				schoolService.getStudentsOfSchool(id));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/findAllSortedBy", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity findAllSortedBy(@RequestParam String sortBy, @RequestParam String direction,
	                                                    @RequestParam(defaultValue = "0") int page,
	                                                    @RequestParam(defaultValue = "5") int size){
		ResponseModel responseModel = new ResponseModel(
				200,
				"find All Students Sorted By " + sortBy + " " + direction + "showing page " + (page+1),
				schoolService.getAllSchoolsSorted(sortBy, direction, page, size));
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value="/searchBy", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity searchBy(@RequestParam Optional<Long> ranking,
	                                             @RequestParam Optional<Integer> numberOfFacultyMembers,
	                                             @RequestParam(defaultValue = "0") int page,
	                                             @RequestParam(defaultValue = "5") int size){
		ResponseModel responseModel = schoolService.searchBy(ranking, numberOfFacultyMembers, page, size);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}

	@RequestMapping(value = "/newCourse", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity newCourse(@RequestBody Course course){
		ResponseModel responseModel = schoolService.newCourse(course);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
}
