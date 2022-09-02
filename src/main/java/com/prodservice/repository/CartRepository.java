package com.prodservice.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.CartEntity;

@Repository
public interface CartRepository extends CrudRepository<CartEntity, String> {

	@Query("SELECT c FROM carts c where  c.userId = :userId")
	List<CartEntity> findCartByuserId(String userId);

	@Query("SELECT c FROM carts c where  c.productName = :productName and c.userId = :userId")
	CartEntity findCartByProductNameAndUserId(String userId, String productName);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE carts c SET c.productQuantity = :quantity where  c.productName = :productName and c.userId = :userId")
	void updateCartByProductNameAndUserId(String userId, String productName, String quantity);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("DELETE FROM carts c where c.userId = :userId and c.productName = :productName")
	void deleteCartByProductNameAndUserId(String productName, String userId);
}