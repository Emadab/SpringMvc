package com.example.demo.Repository;

import com.example.demo.Model.SecurityModels.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

	@Query("select u from User u where u.userName like ?1")
	User findByUserName(String userName);

	@Query
	boolean existsByUserName(String userName);
}
