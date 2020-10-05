package com.example.demo.ServiceImp;

import com.example.demo.Model.SecurityModels.User;
import com.example.demo.Repository.*;
import com.example.demo.Service.StudentService;
import com.example.demo.Util.GlobalFunction;
import com.example.demo.Util.LogMsg;
import com.example.demo.Util.Msg;
import com.example.demo.Model.*;
import com.example.demo.Model.PhoneValidationCode.PhoneValidationCode;
import com.example.demo.Model.ResponseModel.ResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.Model.Student;
import com.example.demo.Model.Parent;
import sun.rmi.runtime.Log;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	Logger logger = Logger.getLogger(StudentServiceImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private ParentRepository parentRepository;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private PhoneValidationCodeRepository phoneValidationCodeRepository;
	@Autowired
	private StudnetImageRepository studnetImageRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private AccessTokenRepository accessTokenRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	public ResponseModel addNewStudent (Student student, int validationCode) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"add new student",
				null);
		//checks sms code
		if(!checkCode(new PhoneValidationCode(student.getPhoneNumber(),
				validationCode)).equals(Msg.smsCodeMatch)){
			responseModel.setResult(Msg.smsCodeNotMatch);
			responseModel.setStatus(-100);
			return responseModel;
		}
		//check if name and lastName are entered
		if(student.getFirstName().isEmpty() || student.getLastName().isEmpty()){
			responseModel.setResult(Msg.noName);
			responseModel.setStatus(-101);
			logger.info(LogMsg.fieldEmpty("name"));
			return responseModel;
		}
		//check if phone is entered
		if(student.getPhoneNumber().isEmpty()){
			responseModel.setResult(Msg.noPhone);
			responseModel.setStatus(-102);
			logger.info(LogMsg.fieldEmpty("phone number"));
			return responseModel;
		}

		if(student.getUserName().isEmpty()){
			responseModel.setResult(Msg.noUsername);
			responseModel.setStatus(-103);
			logger.info(LogMsg.fieldEmpty("username"));
			return responseModel;
		}

		if(student.getAddress().isEmpty()){
			responseModel.setResult(Msg.noAddress);
			responseModel.setStatus(-104);
			logger.info(LogMsg.fieldEmpty("address"));
			return responseModel;
		}

		if(student.getGpa() == -1){
			responseModel.setResult(Msg.noGpa);
			responseModel.setStatus(-105);
			logger.info(LogMsg.fieldEmpty("gpa"));
			return responseModel;
		}

		if(student.getPassword().isEmpty()){
			responseModel.setResult(Msg.noPass);
			responseModel.setStatus(-106);
			logger.info(LogMsg.fieldEmpty("password"));
			return responseModel;
		}
		
		//checks if username is unique
		if(userRepository.existsByUserName(student.getUserName())){
			responseModel.setResult(Msg.usernameTaken);
			responseModel.setStatus(-107);
			return responseModel;
		}
		String encryptedPass = GlobalFunction.stringToMd5(student.getPassword());
		student.setPassword(encryptedPass);
		userRepository.save(new User(student.getUserName(), encryptedPass, "ROLE_STUDENT", false));
		logger.info(LogMsg.addToDb("user"));
		studentRepository.save(student);
		logger.info(LogMsg.addToDb("student"));
		phoneValidationCodeRepository.deleteByPhoneNumber(student.getPhoneNumber());
		logger.info(LogMsg.deleteFromDb("phone validation code"));
		responseModel.setResult(Msg.successSignUp);
		return responseModel;
	}

	@Override
	public List<Student> findAll(int page, int size) {
		return studentRepository.findAll(PageRequest.of(page, size));
}

	@Override
	public String deleteById(int id) {
		userRepository.deleteById(userRepository.findByUserName(studentRepository.findById(id).getUserName()).getId());
		logger.info(LogMsg.deleteFromDb("user"));
		studentRepository.deleteById(id);
		logger.info(LogMsg.deleteFromDb("student"));
		return Msg.successDelete;
	}

	@Override
	public Student getStudent(int id) {
		Student student = studentRepository.findById(id);
		logger.info(LogMsg.retrieveFromDb("student"));
		return student;
	}

	@Override
	public ResponseModel addToSchoolById(int school_id, int student_id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"Add student to school",
				null);
		try{
			School school = entityManager.find(School.class, school_id);
			logger.info(LogMsg.retrieveFromDb("school"));
			Student student = entityManager.find(Student.class, student_id);
			logger.info(LogMsg.retrieveFromDb("student"));
			student.setSchool(school);
			entityManager.persist(student);
			responseModel.setMessage(Msg.successAddStudentToSchool);
			return responseModel;
		}catch (Exception e) {
			responseModel.setStatus(404);
			responseModel.setMessage(Msg.studentNotFound);
			logger.info(LogMsg.notFoundInDb("student"));
			return responseModel;
		}
	}

	@Override
	public ResponseModel addParentToStudentById(Parent parent,
	                                     int student_id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"add parent to student",
				null);
		try{
			Student student = entityManager.find(Student.class, student_id);
			parent.setStudent(student);
			entityManager.persist(parent);
			logger.info(LogMsg.addToDb("parent"));
			responseModel.setMessage(Msg.successAddParentToStudent);
			return responseModel;
		}catch (Exception e) {
			responseModel.setMessage(Msg.studentNotFound);
			responseModel.setStatus(404);
			logger.info(LogMsg.notFoundInDb("student"));
			return responseModel;
		}
	}

	@Override
	public List<Parent> getParentList(int student_id) {
		Student student = entityManager.find(Student.class, student_id);
		Hibernate.initialize(student.getParentList());
		logger.info(LogMsg.retrieveFromDb("parent list"));
		return student.getParentList();
	}

	@Override
	public List<Student> getAllStudentsSorted(String sortBy, String direction,
	                                          int page, int size) {

		direction.toLowerCase();
		logger.info(LogMsg.notFoundInDb("student list"));
		if(direction.equals("asc")){
			return studentRepository.findAllSorted(PageRequest.of(page, size, Sort.by(sortBy).ascending()));
		}
		if(direction.equals("desc")){
			return studentRepository.findAllSorted(PageRequest.of(page, size,
					Sort.by(sortBy).descending()));
		}
			return null;
	}

	@Override
	public ResponseModel searchBy(Optional<String> firstName, Optional<String> lastName,
	                              Optional<String> phoneNumber, Optional<String> address,Optional<String> userName,
	                              Optional<Float> gpa, Optional<String> parentFirstName,
	                              Optional<String> parentLastName, int page,
	                              int size) {

		ResponseModel responseModel = new ResponseModel(
				200,
				"search",
				null);

		Pageable pageable = PageRequest.of(page, size);

		List<Student> studentList1 = null;
		if(parentFirstName.isPresent() || parentLastName.isPresent()) {
			List<Parent> parentList = parentRepository.searchBy(
					parentFirstName.orElse(""),
					parentLastName.orElse(""));
			logger.info(LogMsg.retrieveFromDb("parent list"));
			studentList1 = (getStudentListByParent(parentList));
			logger.info(LogMsg.retrieveFromDb("student list"));
		}
		List<Student> studentList2 = (studentRepository.searchBy(firstName.orElse(""), lastName.orElse(""),
				address.orElse(""), phoneNumber.orElse(""),userName.orElse(""),
				gpa.orElse(-1.0f), pageable));
		logger.info(LogMsg.retrieveFromDb("student list"));
		if(studentList1 == null){
			responseModel.setResult(studentList2);
			return responseModel;
		}
		responseModel.setResult(studentList1.stream()
				.distinct()
				.filter(studentList2::contains)
				.collect(Collectors.toSet()));
		return responseModel;

	}

	private List<Student> getStudentListByParent(List<Parent> parentList) {
		List<Student> studentList = new ArrayList<Student>();
		int length = parentList.size();
		for(int i=0; i<length; i++){
			studentList.add(entityManager.find(Student.class, parentList.get(i).getStudent().getId()));
		}
		logger.info(LogMsg.retrieveFromDb("student list"));
		return studentList;
	}

	@Override
	public ResponseModel changePassword(String oldPassword, String newPassword) {

		ResponseModel responseModel = new ResponseModel(
				200,
				"change password",
				null);



		Student student = studentRepository.findByUserName(GlobalFunction.getUserNameFromAuth());
		logger.info(LogMsg.retrieveFromDb("student"));
		if(student.getPassword().equals(GlobalFunction.stringToMd5(oldPassword))){
			student.setPassword(GlobalFunction.stringToMd5(newPassword));
			studentRepository.save(student);
			responseModel.setMessage(Msg.successPassChange);
			return responseModel;
		}
		responseModel.setStatus(-108);
		responseModel.setMessage(Msg.wrongOldPass);
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


		Student student = studentRepository.findByUserName(userName);
		if(studentRepository.existsByUserName(userName)) {
			if (student.getPassword().equals(GlobalFunction.stringToMd5(password))) {
				try {
					responseModel.setMessage(Msg.successLogin);
					student.setLastLogIn(new Timestamp(System.currentTimeMillis()));
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
					studentRepository.save(student);
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
	public ResponseModel sendMessageCode(String phoneNumber) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"send sms code",
				null);
		int otp = GlobalFunction.generateRandomCode();
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().
				url("https://api.kavenegar.com/v1/39563176702F6646674C79787246" +
						"364F664B77305555714C6B533136446C49616678753978684439732B593D/verify/lookup.json?" +
						"token=" + otp +"&receptor= " + phoneNumber + "&template=mirorim").build();

		try{
			Response response = okHttpClient.newCall(request).execute();
			logger.info(LogMsg.smsCodeReqSent);
			if(response.isSuccessful()){
				PhoneValidationCode phoneValidationCode = new PhoneValidationCode(phoneNumber, otp);
				phoneValidationCodeRepository.save(phoneValidationCode);
				logger.info(LogMsg.addToDb("phone validation code"));
				responseModel.setMessage(Msg.successSmsCodeSent);
				return responseModel;
			}

		}catch (Exception e){
			e.printStackTrace();
			logger.error(e.getStackTrace());
		}
		responseModel.setStatus(-110);
		responseModel.setMessage(Msg.failedSmsCodeSent);
		return responseModel;
	}

	@Override
	public ResponseModel checkCode(PhoneValidationCode phoneValidationCode) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"check sms code",
				null);
		PhoneValidationCode pvcode = phoneValidationCodeRepository.
				findByPhoneNumber(phoneValidationCode.getPhoneNumber());
		if(phoneValidationCode.getCode() == pvcode.getCode()){
			Student s = studentRepository.findByPhoneNumber(pvcode.getPhoneNumber());
			logger.info(LogMsg.retrieveFromDb("student"));
			userRepository.save(userRepository.findByUserName(s.getUserName()).setEnabled(true));
			responseModel.setMessage(Msg.smsCodeMatch);
			return responseModel;
		}
		responseModel.setStatus(-100);
		responseModel.setMessage(Msg.smsCodeNotMatch);
		return responseModel;
	}

	@Override
	public ResponseModel addImage(MultipartFile file, int id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"addImage",
				null);

		if(entityManager.find(Student.class, id).equals(null)){
			responseModel.setStatus(404);
			responseModel.setMessage(Msg.studentNotFound);
			return responseModel;
		}

		if(file.isEmpty()){
			responseModel.setStatus(-115);
			responseModel.setMessage(Msg.noPhoto);
			logger.info(LogMsg.fieldEmpty("photo"));
			return responseModel;
		}

		Student student = entityManager.find(Student.class, id);

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (fileName.contains("/")){
			responseModel.setStatus(-111);
			responseModel.setMessage(Msg.badFileName);
			return responseModel;
		}

		try {
			StudentImage studentImage = new StudentImage(student, fileName, file.getBytes(),
					file.getContentType());
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/student/downloadFile").toUriString() + "/" + student.getId();
			studentImage.setDownloadUri(fileDownloadUri);

			if(student.getStudentImage() != null){
				int oldId = student.getStudentImage().getId();
				student.setStudentImage(null);
				studnetImageRepository.deleteById(oldId);
				logger.info(LogMsg.deleteFromDb("image"));
			}

			student.setStudentImage(studentImage);
			studentImage.setStudent(student);
			entityManager.persist(studentImage);
			logger.info(LogMsg.addToDb("image"));
			responseModel.setMessage(Msg.successImageAdd);
			return responseModel;
		}catch (Exception e){
			e.printStackTrace();
			responseModel.setMessage(Msg.imageIoException);
			logger.error(e.getStackTrace(),e);
			return responseModel;
		}
	}

	@Override
	public ResponseEntity downloadFile(int id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"download file",
				null);

		try{
			Student student = entityManager.find(Student.class, id);
			logger.info(LogMsg.retrieveFromDb("student"));
			StudentImage studentImage = student.getStudentImage();
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(studentImage.getFileType()))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
							+ studentImage.getName() + "\"")
					.body(new ByteArrayResource(studentImage.getImg()));
		}catch (Exception e){
			responseModel.setStatus(404);
			responseModel.setMessage(Msg.studentNotFound);
			logger.info(e.getStackTrace(), e);
			return ResponseEntity.status(404).body(responseModel);
		}
	}

	@Override
	public ResponseModel addCourseToStudentById(int student_id, int course_id) {
		ResponseModel responseModel = new ResponseModel(
				200,
				"add course to student by id",
				null);

		Course course = entityManager.find(Course.class, course_id);
		logger.info(LogMsg.retrieveFromDb("course"));
		Student student = entityManager.find(Student.class, student_id);
		logger.info(LogMsg.retrieveFromDb("student"));

		if(course == null){
			responseModel.setStatus(404);
			responseModel.setMessage(Msg.courseNotFound);
			return responseModel;
		}

		if(student == null){
			responseModel.setStatus(404);
			responseModel.setMessage(Msg.studentNotFound);
			return responseModel;
		}

		course.addStudent(student);
		student.addCourse(course);
		entityManager.persist(course);
		entityManager.persist(student);
		responseModel.setMessage(Msg.successAddCourseToStudent);
		return responseModel;
	}

	@Override
	@Scheduled(cron = "0 0 8 ? * *")
	public void checkStudentActivity() {
		List<Student> studentList =
				studentRepository.findAllByLastLogInBefore(new Timestamp(System.currentTimeMillis() - 86400000));
		for (Student student : studentList) {
			List<Parent> parentList = student.getParentList();
			if(!parentList.isEmpty()) {
				for (Parent parent : parentList) {
					parent.setWarn(true);
					entityManager.persist(parent);
				}
			}
		}
	}

	@Override
	public ResponseModel getProfile() {
		ResponseModel responseModel = new ResponseModel(
				200,
				"profile",
				null);

		responseModel.setResult(studentRepository.findByUserName(GlobalFunction.getUserNameFromAuth()));
		logger.info(LogMsg.retrieveFromDb("student"));
		return responseModel;
	}


}
