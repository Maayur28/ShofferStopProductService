package com.prodservice.repository;

import java.util.List;

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

	@Query("SELECT p FROM products p where  p.productName = :productName")
	ProductEntity findProductByProductName(String productName);

	@Query("SELECT p FROM products p where  p.productCategory = :categoryId and p.prodBrand IN :filters")
	Page<ProductEntity> findProductByCategoryFilter(String categoryId, String[] filters, Pageable pageable);

	@Query("SELECT p FROM products p where  p.productCategory = :categoryId and p.discountedPrice >= :minPrice and p.discountedPrice <= :maxPrice")
	Page<ProductEntity> findProductByCategoryPrice(String categoryId, int minPrice, int maxPrice, Pageable pageable);

	@Query("SELECT p FROM products p where  p.productCategory = :categoryId and p.prodBrand IN :filters and p.discountedPrice >= :minPrice and p.discountedPrice <= :maxPrice")
	Page<ProductEntity> findProductByCategoryFilterPrice(String categoryId, String[] filters, int minPrice,
			int maxPrice, Pageable pageable);

	@Query("SELECT p.prodBrand FROM products p where  p.productCategory = :categoryId")
	List<String> findProductByCategoryBrand(String categoryId);

	@Query("SELECT p FROM products p where UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId)) OR UPPER(p.prodBrand) LIKE concat(upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat(upper(:searchId))"
			+ "OR UPPER(p.productName) LIKE concat('% ',upper(:searchId)) OR UPPER(p.productName) LIKE concat(upper(:searchId),' %') OR UPPER(p.productName) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.productName) LIKE concat(upper(:searchId))")
	Page<ProductEntity> findProductBySearchId(String searchId, Pageable pageable);

	@Query("SELECT p.prodBrand FROM products p where UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId)) OR UPPER(p.prodBrand) LIKE concat(upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat(upper(:searchId))"
			+ "OR UPPER(p.productName) LIKE concat('% ',upper(:searchId)) OR UPPER(p.productName) LIKE concat(upper(:searchId),' %') OR UPPER(p.productName) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.productName) LIKE concat(upper(:searchId))")
	List<String> findBrandsBySearch(String searchId);

	@Query("SELECT p FROM products p where p.prodBrand IN :filters and p.discountedPrice >= :minPrice and p.discountedPrice <= :maxPrice and (UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId)) OR UPPER(p.prodBrand) LIKE concat(upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat(upper(:searchId))"
			+ "OR UPPER(p.productName) LIKE concat('% ',upper(:searchId)) OR UPPER(p.productName) LIKE concat(upper(:searchId),' %') OR UPPER(p.productName) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.productName) LIKE concat(upper(:searchId)))")
	Page<ProductEntity> findProductBySearchFilterPrice(String searchId, String[] filters, int minPrice, int maxPrice,
			Pageable pageable);

	@Query("SELECT p FROM products p where p.prodBrand IN :filters and (UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId)) OR UPPER(p.prodBrand) LIKE concat(upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat(upper(:searchId))"
			+ "OR UPPER(p.productName) LIKE concat('% ',upper(:searchId)) OR UPPER(p.productName) LIKE concat(upper(:searchId),' %') OR UPPER(p.productName) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.productName) LIKE concat(upper(:searchId)))")
	Page<ProductEntity> findProductBySearchFilter(String searchId, String[] filters, Pageable pageable);

	@Query("SELECT p FROM products p where p.discountedPrice >= :minPrice and p.discountedPrice <= :maxPrice and (UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId)) OR UPPER(p.prodBrand) LIKE concat(upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.prodBrand) LIKE concat(upper(:searchId))"
			+ "OR UPPER(p.productName) LIKE concat('% ',upper(:searchId)) OR UPPER(p.productName) LIKE concat(upper(:searchId),' %') OR UPPER(p.productName) LIKE concat('% ',upper(:searchId),' %') OR UPPER(p.productName) LIKE concat(upper(:searchId)))")
	Page<ProductEntity> findProductBySearchPrice(String searchId, int minPrice, int maxPrice, Pageable pageable);
}
