package com.prodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.GiftEntity;

@Repository
public interface GiftRepository extends CrudRepository<GiftEntity, Long> {
	@Query("select g from gifts g where id in :giftIds")
	List<GiftEntity> findGiftsByIds(long[] giftIds);
}
