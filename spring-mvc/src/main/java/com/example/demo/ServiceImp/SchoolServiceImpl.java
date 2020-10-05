package com.example.demo.ServiceImp;

import com.example.demo.Model.PhoneValidationCode.PhoneValidationCode;
import com.example.demo.Model.SecurityModels.User;
import com.example.demo.Repository.CourseRepository;
import com.example.demo.Repository.SchoolRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.SchoolService;
import com.example.demo.Util.GlobalFunction;
import com.example.demo.Util.LogMsg;
import com.example.demo.Util.Msg;
import com.example.demo.Model.Course;
import com.example.demo.Model.ResponseModel.ResponseModel;
import com.example.demo.Model.School;
import com.example.demo.Model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {

	Logger logger = Logger.getLogger(SchoolServiceImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private StudentServiceImpl studentService;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public ResponseModel addNewSchool(School school) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"add new student",
				null);

		if(school.getRanking() == -1){
			responseModel.setResult(Msg.noRanking);
			responseModel.setStatus(-116);
			logger.info(LogMsg.fieldEmpty("ranking"));
			return responseModel;
		}

		if(school.getNumberOfFacultyMembers() == -1){
			responseModel.setResult(Msg.noFaculty);
			responseModel.setStatus(-117);
			logger.info(LogMsg.fieldEmpty("number of faculty"));
			return responseModel;
		}

		if(school.getNumberOfStudents() == -1){
			responseModel.setResult(Msg.noStudent);
			responseModel.setStatus(-118);
			logger.info(LogMsg.fieldEmpty("number of students"));
			return responseModel;
		}
		//check if phone is entered
		if(school.getPhoneNumber().isEmpty()){
			responseModel.setResult(Msg.noPhone);
			responseModel.setStatus(-102);
			logger.info(LogMsg.fieldEmpty("phone number"));
			return responseModel;
		}

		if(school.getUserName().isEmpty()){
			responseModel.setResult(Msg.noUsername);
			responseModel.setStatus(-103);
			logger.info(LogMsg.fieldEmpty("username"));
			return responseModel;
		}

		if(school.getAddress().isEmpty()){
			responseModel.setResult(Msg.noAddress);
			responseModel.setStatus(-104);
			logger.info(LogMsg.fieldEmpty("address"));
			return responseModel;
		}

		if(school.getPassword().isEmpty()){
			responseModel.setResult(Msg.noPass);
			responseModel.setStatus(-106);
			logger.info(LogMsg.fieldEmpty("password"));
			return responseModel;
		}

		//checks if username is unique
		if(userRepository.existsByUserName(school.getUserName())){
			responseModel.setResult(Msg.usernameTaken);
			responseModel.setStatus(-107);
			return responseModel;
		}
		String encryptedPass = GlobalFunction.stringToMd5(school.getPassword());
		school.setPassword(encryptedPass);
		userRepository.save(new User(school.getUserName(), encryptedPass, "ROLE_SCHOOL", true));
		logger.info(LogMsg.addToDb("user"));
		schoolRepository.save(school);
		logger.info(LogMsg.addToDb("student"));
		responseModel.setResult(Msg.successSignUp);

		return responseModel;
	}

	@Override
	public Iterable<School> findAll(int page, int size) {
		return schoolRepository.findAll(PageRequest.of(page, size));
	}

	@Override
	public String deleteById(int id) {
		userRepository.deleteById(userRepository.findByUserName(
				schoolRepository.findById(id).get().getUserName()).getId());
		logger.info(LogMsg.deleteFromDb("user"));
		schoolRepository.deleteById(id);
		logger.info(LogMsg.deleteFromDb("school"));
		return Msg.successDelete;
	}

	@Override
	public ResponseModel addStudentById(int school_id, int student_id) {
		ResponseModel responseModel = new ResponseModel(
				404,
				Msg.schoolNotFound,
				null);
		if (schoolRepository.existsById(school_id)) {
			return studentService.addToSchoolById(school_id, student_id);
		}
		return responseModel;
	}

	@Override
	public List<Student> getStudentsOfSchool(int school_id) {
		School school = entityManager.find(School.class, school_id);
		Hibernate.initialize(school.getStudentList());
		logger.info(LogMsg.retrieveFromDb("student list"));
		return school.getStudentList();
	}

	@Override
	public List<School> getAllSchoolsSorted(String sortBy, String direction, int page, int size) {
		direction.toLowerCase();
		logger.info(LogMsg.retrieveFromDb("student list"));
		if (direction.equals("asc")) {
			return schoolRepository.findAllSorted(PageRequest.of(page, size, Sort.by(sortBy).ascending()));
		}
		if (direction.equals("desc")) {
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

		logger.info(LogMsg.retrieveFromDb("school list"));
		if (ranking.isPresent()) {
			if (numberOfFacultyMembers.isPresent()) {
				responseModel.setResult(schoolRepository.findAllByNumberOfFacultyMembersAndRanking(ranking.get(),
						numberOfFacultyMembers.get(), pageable));
				return responseModel;
			} else {
				responseModel.setResult(schoolRepository.findAllByRanking(ranking.get(), pageable));
				return responseModel;
			}
		} else if (numberOfFacultyMembers.isPresent()) {
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

		if (course.getName().isEmpty()) {
			responseModel.setStatus(-114);
			responseModel.setMessage(Msg.noCourseName);
			logger.info(LogMsg.fieldEmpty("name"));
			return responseModel;
		}
		if (course.getTeacher().isEmpty()) {
			responseModel.setStatus(-112);
			responseModel.setMessage(Msg.noCourseTeacher);
			logger.info(LogMsg.fieldEmpty("teacher"));
			return responseModel;
		}
		if (course.getUnit() == -1) {
			responseModel.setStatus(-113);
			responseModel.setMessage(Msg.noCourseUnitNo);
			logger.info(LogMsg.fieldEmpty("unit"));
			return responseModel;
		}

		entityManager.persist(course);
		responseModel.setMessage(Msg.successAddCourse);
		logger.info(LogMsg.addToDb("course"));
		return responseModel;
	}

	@Override
	public ResponseModel signIn(String userName, String password) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"sign-in",
				null);

		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(2, TimeUnit.MINUTES);
		client.setReadTimeout(2, TimeUnit.MINUTES);


		School school = schoolRepository.findByUserName(userName);
		if (schoolRepository.existsByUserName(userName)) {
			if (school.getPassword().equals(GlobalFunction.stringToMd5(password))) {
				try {
					responseModel.setMessage(Msg.successLogin);
					Request request = new Request.Builder()
							.url("http://localhost:8080/oauth/" +
									"token?client_id=adminapp" +
									"&client_secret=adminapp" +
									"&grant_type=password" +
									"&username=" + userName +
									"&password=" + GlobalFunction.stringToMd5(password))
							.post(RequestBody.create(null, new byte[0]))
							.build();
					ObjectMapper mapper = new ObjectMapper();
					logger.info(LogMsg.authReqSent);
					responseModel.setResult(mapper.readTree(client.newCall(request).execute().body().string()));
					schoolRepository.save(school);
					return responseModel;
				} catch (Exception e) {
					e.printStackTrace();
					responseModel.setStatus(-1);
					responseModel.setMessage("exception");
					logger.error(e.getStackTrace());
					return responseModel;
				}
			}
		}
		responseModel.setStatus(-109);
		responseModel.setMessage(Msg.failedLogin);
		logger.info(LogMsg.failedLogin);
		return responseModel;
	}

	@Override
	public ResponseModel getStudents() {
		ResponseModel responseModel = new ResponseModel(
				200,
				"students",
				null);

		School school = schoolRepository.findByUserName(GlobalFunction.getUserNameFromAuth());
		Hibernate.initialize(school.getStudentList());
		logger.info(LogMsg.retrieveFromDb("student list"));
		responseModel.setResult(school.getStudentList());
		return responseModel;
	}
}
