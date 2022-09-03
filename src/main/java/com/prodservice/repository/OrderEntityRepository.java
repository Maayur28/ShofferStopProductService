package com.prodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.OrderIdEntity;

@Repository
public interface OrderEntityRepository extends CrudRepository<OrderIdEntity, String> {

	@Query("select o from orderitems o where id in :itemIds")
	List<OrderIdEntity> findItemsByIds(long[] itemIds);

	@Query("select o from orderitems o where id in :itemId")
	OrderIdEntity findItemsById(long itemId);
}