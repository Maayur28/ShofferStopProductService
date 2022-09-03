package com.prodservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.OrderEntity;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, String> {
	
	Page<OrderEntity> findPaginatedByUserId(String userId, Pageable pageable);
	
}
