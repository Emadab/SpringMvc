package com.example.demo.ServiceImp;

import com.example.demo.Repository.CourseRepository;
import com.example.demo.Repository.SchoolRepository;
import com.example.demo.Service.SchoolService;
import com.example.demo.Util.Msg;
import com.example.demo.Model.Course;
import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.School;
import com.example.demo.Model.Student;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private StudentServiceImpl studentService;

	@Autowired
	private CourseRepository courseRepository;

	@Override
	public String addNewSchool(int numberOfStudents, int numberOfFacultyMembers, long ranking, String address,
	                           String phoneNumber) {
		School school = new School(numberOfStudents, numberOfFacultyMembers, ranking, address, phoneNumber);
		schoolRepository.save(school);
		return Msg.saved;
	}

	@Override
	public Iterable<School> findAll(int page, int size) {
		return schoolRepository.findAll(PageRequest.of(page, size));
	}

	@Override
	public String deleteById(int id) {
		schoolRepository.deleteById(id);
		return Msg.successDelete;
	}

	@Override
	public ResponseModel addStudentById(int school_id, int student_id) {
		ResponseModel responseModel = new ResponseModel(
				404,
				Msg.schoolNotFound,
				null);
		if(schoolRepository.existsById(school_id)){
			return studentService.addToSchoolById(school_id, student_id);
		}
		return responseModel;
	}

	@Override
	public List<Student> getStudentsOfSchool(int school_id) {
		School school = entityManager.find(School.class, school_id);
		Hibernate.initialize(school.getStudentList());
		return school.getStudentList();
	}

	@Override
	public List<School> getAllSchoolsSorted(String sortBy, String direction, int page, int size) {
		direction.toLowerCase();
		if(direction.equals("asc")){
			return schoolRepository.findAllSorted(PageRequest.of(page, size, Sort.by(sortBy).ascending()));
		}
		if(direction.equals("desc")){
			return schoolRepository.findAllSorted(PageRequest.of(page, size, Sort.by(sortBy).descending()));
		}
		return null;
	}

	@Override
	public ResponseModel searchBy(Optional<Long> ranking, Optional<Integer> numberOfFacultyMembers,
	                             int page, int size) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"search",
				null);

		Pageable pageable = PageRequest.of(page, size);

		if(ranking.isPresent()){
			if(numberOfFacultyMembers.isPresent()){
				responseModel.setResult(schoolRepository.findAllByNumberOfFacultyMembersAndRanking(ranking.get(),
						numberOfFacultyMembers.get(), pageable));
				return responseModel;
			}
			else{
				responseModel.setResult(schoolRepository.findAllByRanking(ranking.get(), pageable));
				return responseModel;
			}
		}
		else if(numberOfFacultyMembers.isPresent()){
			responseModel.setResult(schoolRepository.
					findAllByNumberOfFacultyMembers(numberOfFacultyMembers.get(), pageable));
			return responseModel;
		}
		responseModel.setResult((List<School>) schoolRepository.findAll());
		return responseModel;
	}

	@Override
	public ResponseModel newCourse(Course course) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"new course",
				null);

		if(course.getName().isEmpty()){
			responseModel.setStatus(-114);
			responseModel.setMessage(Msg.noCourseName);
			return responseModel;
		}
		if(course.getTeacher().isEmpty()){
			responseModel.setStatus(-112);
			responseModel.setMessage(Msg.noCourseTeacher);
			return responseModel;
		}
		if(course.getUnit() == -1){
			responseModel.setStatus(-113);
			responseModel.setMessage(Msg.noCourseUnitNo);
			return responseModel;
		}

		entityManager.persist(course);
		responseModel.setMessage(Msg.successAddCourse);
		return responseModel;
	}
}
