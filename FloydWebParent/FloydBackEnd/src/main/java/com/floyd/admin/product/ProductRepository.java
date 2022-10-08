package com.floyd.admin.product;

import com.floyd.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    public Long countById(Integer id);

    public Product findByName(String name);

    @Query("UPDATE Product p SET p.enabled = :enabled WHERE p.id = :id")
    @Modifying
    public void updateEnabledStatus(@Param("id") Integer id, @Param("enabled") boolean enabled);

    @Query("SELECT p FROM Product p WHERE CONCAT(p,name, ' ', p.shortDescription, ' ', p.fullDescription, ' ', p.brand.name, ' ', p.category.name) LIKE %:keyword%")
    public Page<Product> findAll(@Param("keyword") String keyword, Pageable pageable);
}
