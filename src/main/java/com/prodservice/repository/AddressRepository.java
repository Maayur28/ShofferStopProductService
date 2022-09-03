package com.prodservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.UserAddressEntity;

@Repository
public interface AddressRepository extends CrudRepository<UserAddressEntity, String> {

}
