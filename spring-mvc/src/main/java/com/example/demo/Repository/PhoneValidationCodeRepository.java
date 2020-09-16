package com.example.demo.Repository;

import com.example.demo.Model.PhoneValidationCode.PhoneValidationCode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PhoneValidationCodeRepository extends CrudRepository<PhoneValidationCode, Integer> {

	@Query("select p from PhoneValidationCode p where p.phoneNumber like %?1")
	PhoneValidationCode findByPhoneNumber(String phoneNumber);

	@Transactional
	@Modifying
	@Query("delete from PhoneValidationCode p where p.phoneNumber=:phoneNumber")
	int deleteByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
