package com.prodservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.GiftEntity;

@Repository
public interface GiftRepository extends CrudRepository<GiftEntity,String> {
	
}
