package com.example.demo.Repository;

import com.example.demo.Model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface StudentRepository extends CrudRepository<Student,Integer> {

	@Query("FROM Student")
	List<Student> findAll(Pageable pageable);

	@Query("select s from Student s where s.phoneNumber like ?1")
	Student findByPhoneNumber(String phoneNumber);

	@Query ("select s from Student s where s.id = ?1")
	Student findById(int id);

	@Query("FROM Student")
	List<Student> findAllSorted(Pageable pageable);

	@Query("select s from Student s where s.firstName like %?1% and s.lastName like %?2% and s.address like " +
			"%?3% and s.phoneNumber like %?4% and " +
			"s.userName like %?5% and (abs(s.gpa-?6) <= 1e-6 or abs(?6+1.0) <= 1e-6)")
	List<Student> searchBy(String firstName, String lastName, String address,
	                        String phoneNumber,String userName, float gpa, Pageable pageable);

	@Query("select s from Student s where s.userName like %?1")
	Student findByUserName(String userName);

	@Query
	boolean existsByUserName(String userName);

	@Query("select s from Student s where s.lastLogIn <= ?1")
	List<Student> findAllByLastLogInBefore(Timestamp timestamp);
}
