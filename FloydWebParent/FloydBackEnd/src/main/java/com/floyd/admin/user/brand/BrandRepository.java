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
    public Page<Brand> listBrands(Pageable pageable);

    @Query("SELECT b FROM Brand b WHERE CONCAT(b.id, ' ', b.name) LIKE %:keyword%")
    public Page<Brand> search(String keyword, Pageable pageable);
}
