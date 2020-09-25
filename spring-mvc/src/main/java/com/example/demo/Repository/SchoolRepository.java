package com.example.demo.Repository;

import com.example.demo.Model.School;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchoolRepository extends CrudRepository<School,Integer> {

	@Query("FROM School")
	List<School> findAll(Pageable pageable);

	@Query("FROM School")
	List<School> findAllSorted(Pageable pageable);

	@Query("select s from School s where s.ranking=?1")
	List<School> findAllByRanking(long ranking, Pageable pageable);

	@Query("select s from School s where s.numberOfFacultyMembers=?1")
	List<School> findAllByNumberOfFacultyMembers(int numberOfFacultyMembers, Pageable pageable);

	@Query("select s from School s where s.ranking=?1 and s.numberOfFacultyMembers=?2")
	List<School> findAllByNumberOfFacultyMembersAndRanking(long ranking, int numberOfFacultyMembers, Pageable pageable);

	@Query("select s from School s where s.userName like ?1")
	School findByUserName(String userName);

	@Query
	boolean existsByUserName(String userName);
}
