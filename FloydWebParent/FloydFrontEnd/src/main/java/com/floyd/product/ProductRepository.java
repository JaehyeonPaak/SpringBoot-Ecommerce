package com.floyd.product;

import com.floyd.common.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true ORDER BY p.name ASC")
    public List<Product> findAllEnabled();
}
