package com.prodservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.prodservice.entity.ProductEntity;

@Repository
public interface ProdRepository extends PagingAndSortingRepository<ProductEntity, String> {
	
	@Query("SELECT p FROM products p where  p.productCategory = :categoryId")
	Page<ProductEntity> findProductByCategory(String categoryId, Pageable pageable);
}
