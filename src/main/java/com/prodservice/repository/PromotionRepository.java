package com.prodservice.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.PromotionEntity;

@Repository
public interface PromotionRepository extends CrudRepository<PromotionEntity, String> {

	@Query("Select p from promotions p where p.promoId = :promoId")
	PromotionEntity findByPromoId(String promoId);

	@Modifying
	@Transactional
	@Query("Update promotions p SET p.at99 = :product99 , p.at499 = :product499 , p.at999 = :product999 , p.bogo = :productBogo , p.dotd = :productDotd , p.promoDate = :date where p.promoId = :promoId")
	void updatePromo(String promoId, String product99, String product499, String product999, String productBogo,
			String productDotd,String date);

}
