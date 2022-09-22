package com.floyd.admin.user.category.controller;

import com.floyd.admin.user.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories/check_unique")
    public void checkUnique(@Param("name") String name, @Param("alias") String alias) {

    }
}
