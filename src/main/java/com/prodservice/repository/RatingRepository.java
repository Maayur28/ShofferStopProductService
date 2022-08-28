package com.prodservice.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.RatingEntity;

@Repository
public interface RatingRepository extends CrudRepository<RatingEntity, String> {

	@Query("SELECT r FROM ratings r where  r.productName = :productName")
	List<RatingEntity> findRatingByProductName(String productName);

	@Query("SELECT r FROM ratings r where  r.productName = :productName and r.userId = :userId")
	RatingEntity findRatingByProductNameAndUserId(String userId, String productName);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE ratings r SET r.rating = :rating where  r.productName = :productName and r.userId = :userId")
	void updateRatingByProductNameAndUserId(String userId, String productName, Integer rating);
}
