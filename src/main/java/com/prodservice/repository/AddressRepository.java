package com.prodservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.UserAddressEntity;

@Repository
public interface AddressRepository extends CrudRepository<UserAddressEntity, String> {

	@Query("SELECT a.fullName FROM address a WHERE a.id = :id")
	String findFullNameById(long id);
}
