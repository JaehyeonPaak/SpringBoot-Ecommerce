package com.floyd.admin.user.brand;

import com.floyd.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
    public Long countById(Integer id);

    public Brand findByName(String name);

    @Query("SELECT b FROM Brand b")
    Page<Brand> listBrands(Pageable pageable);
}
