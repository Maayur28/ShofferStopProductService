package com.prodservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.OrderIdEntity;

@Repository
public interface OrderEntityRepository extends CrudRepository<OrderIdEntity, String> {
	
}
