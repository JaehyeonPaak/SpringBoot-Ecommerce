package com.floyd.product;

import com.floyd.common.entity.Category;
import com.floyd.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = :category_id OR p.category.allParentIDs LIKE %:category_id_match%) ORDER BY p.name ASC")
    public Page<Product> listByCategory(@Param("category_id") Integer categoryId, @Param("category_id_match") String categoryIDMatch, Pageable pageable);
}
