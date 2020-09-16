package com.example.demo.Repository;

import com.example.demo.Model.Parent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParentRepository extends CrudRepository<Parent,Integer> {

	@Query("select p from Parent p where p.firstName like %?1% and p.lastName like %?2%")
	List<Parent> searchBy(String firstName, String lastName);
}
