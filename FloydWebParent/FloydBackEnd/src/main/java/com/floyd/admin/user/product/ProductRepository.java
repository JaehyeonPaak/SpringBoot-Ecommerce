package com.floyd.admin.user.product;

import com.floyd.common.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    public Long countById(Integer id);
}
