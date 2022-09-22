package com.floyd.admin.user.category;

import com.floyd.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    public List<Category> listRootCategories();

    public Category findByName(String name);

    public Category findByAlias(String alias);
}
