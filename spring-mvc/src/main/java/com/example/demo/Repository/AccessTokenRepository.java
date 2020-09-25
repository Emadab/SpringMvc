package com.example.demo.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.Model.SecurityModels.OAuthAccessToken;

public interface AccessTokenRepository extends CrudRepository<OAuthAccessToken, String> {
	@Query("select t from OAuthAccessToken t where t.id like ?1")
	OAuthAccessToken findByToken(String token);
}
