package com.floyd.admin.category;

import com.floyd.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    public List<Category> listRootCategories(Sort sort);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    public Page<Category> listRootCategories(Pageable pageable);

    public Category findByName(String name);

    public Category findByAlias(String alias);

    @Query("UPDATE Category c SET c.enabled = :enabled WHERE c.id = :id")
    @Modifying
    public void updateEnabledStatus(@Param("id") Integer id, @Param("enabled") boolean enabled);

    public Long countById(Integer id);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword%")
    public Page<Category> search(@Param("keyword") String keyword, Pageable pageable);
}
